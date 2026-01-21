package pt.procurainterna.guru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import pt.procurainterna.guru.leetcode.DailyChallengeFetcher;
import pt.procurainterna.guru.leetcode.LeetcodeChallengePoster;
import pt.procurainterna.guru.leetcode.model.DailyChallengeResponse;

public class PostDailyChallengeCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostDailyChallengeCommand.class);

  private final DailyChallengeFetcher dailyChallengeFetcher;
  private final LeetcodeChallengePoster challengePoster;

  @Inject
  public PostDailyChallengeCommand(DailyChallengeFetcher dailyChallengeFetcher,
      LeetcodeChallengePoster challengePoster) {
    this.dailyChallengeFetcher = dailyChallengeFetcher;
    this.challengePoster = challengePoster;
  }

  public void execute(SlashCommandInteractionEvent event) {
    LOGGER.info("Posting daily challenge");
    final DailyChallengeResponse dailyChallenge = dailyChallengeFetcher.fetch();
    LOGGER.info("Daily challenge fetched: {}", dailyChallenge);
    challengePoster.post(dailyChallenge);
    LOGGER.info("Daily challenge posted");
  }

}
