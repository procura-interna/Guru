package pt.procurainterna.guru.leetcode;

import jakarta.inject.Inject;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import pt.procurainterna.guru.persistance.DailyChallengeConfigRepository;
import pt.procurainterna.guru.persistance.GuildDailyChallengeConfig;

public class JdaLeetcodeChallengePoster implements LeetcodeChallengePoster {

  private final JDA jda;
  private final DailyChallengeConfigRepository configRepository;

  @Inject
  public JdaLeetcodeChallengePoster(JDA jda, DailyChallengeConfigRepository configRepository) {
    this.jda = jda;
    this.configRepository = configRepository;
  }

  @Override
  public void post(DailyChallengeResponse dailyChallengeResponse) {
    final MessageEmbed embed = toEmbed(dailyChallengeResponse);

    jda.getGuilds().forEach(g -> {
      final String guildId = g.getId();

      final Optional<GuildDailyChallengeConfig> config = configRepository.forGuild(guildId);

      config.map(GuildDailyChallengeConfig::getChannelId).map(g::getTextChannelById)
          .ifPresent(textChannel -> textChannel.sendMessageEmbeds(embed).queue());
    });
  }

  private MessageEmbed toEmbed(DailyChallengeResponse dailyChallenge) {
    Objects.requireNonNull(dailyChallenge, "resp");

    var q = Objects.requireNonNull(dailyChallenge.getData(), "resp.data")
        .getActiveDailyCodingChallengeQuestion();

    if (q == null) {
      return new EmbedBuilder().setTitle("LeetCode Daily")
          .setDescription("No daily challenge available in the response.").build();
    }

    var question = q.getQuestion();

    LocalDate dateStr = q.getDate(); // "2026-01-19"
    String formattedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(dateStr);

    String title = (question != null && question.getTitle() != null) ? question.getTitle()
        : "LeetCode Daily Challenge";

    String difficulty = question.getDifficulty();
    String id = question.getFrontendQuestionId();

    String url = "https://leetcode.com" + q.getLink();

    String displayTitle = (id.isBlank() ? title : (id + ". " + title));

    String desc =
        "Difficulty: **" + (difficulty.isBlank() ? "—" : difficulty) + "**" + "\nDate: **" + (
            formattedDate.isBlank() ? "—" : formattedDate) + "**";

    return new EmbedBuilder().setTitle("🧩 LeetCode Daily — " + displayTitle, url)
        .setDescription(desc).addField("Link", url, false).setFooter("LeetCode Daily Challenge")
        .setColor(new Color(255, 161, 22))
        .build();
  }
}
