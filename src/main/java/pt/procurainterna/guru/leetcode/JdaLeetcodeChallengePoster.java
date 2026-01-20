package pt.procurainterna.guru.leetcode;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import jakarta.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import pt.procurainterna.guru.leetcode.model.DailyChallengeResponse;
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
    Objects.requireNonNull(dailyChallenge, "dailyChallenge");

    final var challengeData =
        Objects.requireNonNull(dailyChallenge.getData(), "dailyChallenge.data")
            .getActiveDailyCodingChallengeQuestion();

    if (challengeData == null) {
      return new EmbedBuilder().setTitle("LeetCode Daily")
          .setDescription("No daily challenge available in the response.").build();
    }

    final var question = challengeData.getQuestion();
    Objects.requireNonNull(question, "challengeData.question");

    final LocalDate dateStr = challengeData.getDate();
    final String formattedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(dateStr);

    final String title =
        (question.getTitle() != null) ? question.getTitle() : "LeetCode Daily Challenge";

    final String difficulty = question.getDifficulty();
    final String id = question.getFrontendQuestionId();

    final String url = "https://leetcode.com" + challengeData.getLink();

    final String displayTitle = (id.isBlank() ? title : (id + ". " + title));

    final String desc = "Difficulty: **" + (difficulty.isBlank() ? "—" : difficulty) + "**"
        + "\nDate: **" + (formattedDate.isBlank() ? "—" : formattedDate) + "**";

    return new EmbedBuilder().setTitle("🧩 LeetCode Daily — " + displayTitle, url)
        .setDescription(desc).addField("Link", url, false).setFooter("LeetCode Daily Challenge")
        .setColor(new Color(255, 161, 22)).build();
  }
}
