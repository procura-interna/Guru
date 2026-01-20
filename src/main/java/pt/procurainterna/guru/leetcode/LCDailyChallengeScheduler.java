package pt.procurainterna.guru.leetcode;

import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import pt.procurainterna.guru.cdi.CDIProvider;
import pt.procurainterna.guru.leetcode.model.DailyChallengeResponse;

public class LCDailyChallengeScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyChallengeScheduler.class);

  private final CDIProvider cdiProvider;

  @Inject
  public LCDailyChallengeScheduler(CDIProvider cdiProvider) {
    this.cdiProvider = cdiProvider;
  }

  public void schedule() {
    final JobDetail job = JobBuilder.newJob(DailyChallengeJob.class)
        .withIdentity("dailyChallengeJob", "daily").build();


    final CronScheduleBuilder schedule = CronScheduleBuilder.dailyAtHourAndMinute(23, 16)
        .inTimeZone(TimeZone.getTimeZone("Europe/Lisbon"));
    final Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("dailyChallengeTrigger", "daily").withSchedule(schedule).build();

    final Scheduler scheduler = cdiProvider.getInstance(Scheduler.class);
    LOGGER.info("Scheduler obtained");

    try {
      scheduler.scheduleJob(job, trigger);
      LOGGER.info("Scheduled daily challenge job");

    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }


  public static class DailyChallengeJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyChallengeJob.class);

    @Inject
    DailyChallengeFetcher dailyChallengeFetcher;
    @Inject
    LeetcodeChallengePoster leetcodeChallengePoster;

    @Override
    public void execute(JobExecutionContext context) {
      LOGGER.info("Executing daily challenge job");
      final DailyChallengeResponse dailyChallengeResponse = dailyChallengeFetcher.fetch();
      leetcodeChallengePoster.post(dailyChallengeResponse);
      LOGGER.info("Daily challenge job executed");
    }
  }

}
