package pt.procurainterna.guru.cdi;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.hc.client5.http.classic.HttpClient;
import org.jdbi.v3.core.Jdbi;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.inject.AbstractModule;

import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import pt.procurainterna.guru.HttpClientProvider;
import pt.procurainterna.guru.leetcode.DailyChallengeFetcher;
import pt.procurainterna.guru.leetcode.HttpClientDailyChallengeFetcher;
import pt.procurainterna.guru.leetcode.JdaLeetcodeChallengePoster;
import pt.procurainterna.guru.leetcode.LeetcodeChallengePoster;
import pt.procurainterna.guru.persistance.DailyChallengeConfigRepository;
import pt.procurainterna.guru.persistance.DbInitializer;
import pt.procurainterna.guru.persistance.DefaultDbInitializer;
import pt.procurainterna.guru.persistance.GuildInitialRoleRepository;
import pt.procurainterna.guru.persistance.JdbiDailyChallengeConfigRepository;
import pt.procurainterna.guru.persistance.JdbiGuildInitialRoleRepository;

public class GuruCdiModule extends AbstractModule {

  static final Logger LOGGER = LoggerFactory.getLogger(GuruCdiModule.class);

  private final Jdbi jdbi;
  private final JDA jda;

  public GuruCdiModule(Jdbi jdbi, JDA jda) {
    this.jdbi = jdbi;
    this.jda = jda;
  }

  @Override
  protected void configure() {
    bind(Jdbi.class).toInstance(jdbi);
    bind(JDA.class).toInstance(jda);
    bind(CDIProvider.class).to(GuiceCDIProvider.class);
    bind(Gson.class).toInstance(gson());
    bind(Scheduler.class).toProvider(SchedulerProvider.class).in(Singleton.class);
    bind(GuildInitialRoleRepository.class).to(JdbiGuildInitialRoleRepository.class);
    bind(DbInitializer.class).to(DefaultDbInitializer.class);
    bind(HttpClient.class).toProvider(HttpClientProvider.class).in(Singleton.class);
    bind(DailyChallengeConfigRepository.class).to(JdbiDailyChallengeConfigRepository.class);
    bind(DailyChallengeFetcher.class).to(HttpClientDailyChallengeFetcher.class);
    bind(LeetcodeChallengePoster.class).to(JdaLeetcodeChallengePoster.class);
  }

  private Gson gson() {
    final GsonBuilder builder = new GsonBuilder();

    builder.registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
      @Override
      public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
          out.nullValue();
          return;
        }
        out.value(value.toString());
      }

      @Override
      public LocalDate read(JsonReader in) throws IOException {
        return switch (in.peek()) {
          case NULL -> {
            in.nextNull();
            yield null;
          }
          default -> LocalDate.parse(in.nextString());
        };
      }
    });

    return builder.create();
  }

}
