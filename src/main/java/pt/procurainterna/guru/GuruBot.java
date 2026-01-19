package pt.procurainterna.guru;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import pt.procurainterna.guru.cdi.GuruCdiModule;
import pt.procurainterna.guru.jda.events.EntryPointEventListener;
import pt.procurainterna.guru.jda.events.EntryPointSlashCommandConsumer;
import pt.procurainterna.guru.leetcode.DailyChallengeResponse;
import pt.procurainterna.guru.leetcode.HttpClientDailyChallengeFetcher;
import pt.procurainterna.guru.persistance.DailyChallengeConfigRepository;
import pt.procurainterna.guru.persistance.DbInitializer;
import pt.procurainterna.guru.persistance.GuildDailyChallengeConfig;
import pt.procurainterna.guru.persistance.JdbcConfig;
import pt.procurainterna.guru.persistance.hikari.ConfigToHikariDataSource;

public class GuruBot {

  private static final Logger logger = LoggerFactory.getLogger(GuruBot.class);

  public Future<Void> start(final GuruParameters parameters) {
    logger.info("Initializing JDA with token: {}", parameters.apiToken != null ? "******" : "null");
    final CompletableFuture<Void> future = new CompletableFuture<>();

    final Jdbi jdbi = jdbi(parameters.jdbcConfig);

    final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "leetcode-daily-scheduler");
      t.setDaemon(false);
      return t;
    });

    final Injector injector = Guice.createInjector(new GuruCdiModule(jdbi));

    injector.getInstance(DbInitializer.class).initialize();

    final EntryPointEventListener entryPointEventListener =
        new EntryPointEventListener(() -> future.complete(null),
            new EntryPointSlashCommandConsumer(injector), new GuildReadyListener());

    final JDA jda = JDABuilder.createDefault(parameters.apiToken)
        .addEventListeners(entryPointEventListener).build();

    final Runnable leetcodeDailyChallengeFetch = () -> {
      final DailyChallengeResponse dailyChallengeResponse;
      try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
        final HttpClientDailyChallengeFetcher httpClientDailyChallengeFetcher =
            new HttpClientDailyChallengeFetcher(httpClient);

        dailyChallengeResponse = httpClientDailyChallengeFetcher.fetch();

      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      final MessageEmbed messageEmbed = toEmbed(dailyChallengeResponse);

      final DailyChallengeConfigRepository configRepository =
          injector.getInstance(DailyChallengeConfigRepository.class);



      jda.getGuilds().forEach(g -> {
        final String guildId = g.getId();

        final Optional<GuildDailyChallengeConfig> config =
            configRepository.forGuild(guildId);

        config.ifPresent(c -> {
          final TextChannel textChannel = g.getTextChannelById(c.getChannelId());
          textChannel.sendMessageEmbeds(messageEmbed).queue();
        });
      });

    };

    future.whenComplete((ok, ex) -> {
      if (ex != null) {
        jda.shutdown();

      } else {
        logger.info("GuruBot finished successfully");
      }
    });

    return future;
  }

  private MessageEmbed toEmbed(DailyChallengeResponse dailyChallenge) {
    Objects.requireNonNull(dailyChallenge, "resp");

    var q = Objects.requireNonNull(dailyChallenge.getData(), "resp.data")
        .getActiveDailyCodingChallengeQuestion();

    if (q == null) {
      return new EmbedBuilder()
          .setTitle("LeetCode Daily")
          .setDescription("No daily challenge available in the response.")
          .build();
    }

    var question = q.getQuestion();

    LocalDate dateStr = q.getDate(); // "2026-01-19"
    String formattedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(dateStr);

    String title = (question != null && question.getTitle() != null)
        ? question.getTitle()
        : "LeetCode Daily Challenge";

    String difficulty = question.getDifficulty();
    String id = question.getFrontendQuestionId();

    String url = "https://leetcode.com" + q.getLink();

    String displayTitle = (id.isBlank() ? title : (id + ". " + title));

    String desc = "Difficulty: **" + (difficulty.isBlank() ? "—" : difficulty) + "**"
        + "\nDate: **" + (formattedDate.isBlank() ? "—" : formattedDate) + "**";

    return new EmbedBuilder()
        .setTitle("🧩 LeetCode Daily — " + displayTitle, url)
        .setDescription(desc)
        .addField("Link", url, false)
        .setFooter("LeetCode Daily Challenge")
        .build();
  }

  private Jdbi jdbi(JdbcConfig jdbcConfig) {
    final DataSource dataSource = new ConfigToHikariDataSource().dataSource(jdbcConfig);

    return Jdbi.create(dataSource);
  }
}
