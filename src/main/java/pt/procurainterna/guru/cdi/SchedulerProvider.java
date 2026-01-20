package pt.procurainterna.guru.cdi;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.google.inject.Provider;

import jakarta.inject.Inject;
import pt.procurainterna.guru.quartz.CdiJobFactory;

final class SchedulerProvider implements Provider<Scheduler> {

  private final CDIProvider cdiProvider;

  @Inject
  public SchedulerProvider(CDIProvider cdiProvider) {
    this.cdiProvider = cdiProvider;
  }

  @Override
  public Scheduler get() {
    final Scheduler scheduler;
    try {
      scheduler = StdSchedulerFactory.getDefaultScheduler();

    } catch (SchedulerException e) {
      throw new RuntimeException("Failed to obtain default scheduler", e);
    }

    GuruCdiModule.LOGGER.info("Default scheduler obtained");



    try {
      scheduler.setJobFactory(new CdiJobFactory(cdiProvider));

    } catch (SchedulerException e) {
      throw new RuntimeException("Failed to set CDI enabled job factory", e);
    }


    try {
      if (!scheduler.isStarted()) {
        try {
          scheduler.start();

        } catch (SchedulerException e) {
          throw new RuntimeException("Failed to start scheduler", e);
        }
      }
    } catch (SchedulerException e) {
      throw new RuntimeException("Failed to start scheduler", e);
    }

    GuruCdiModule.LOGGER.info("Default scheduler started");

    return scheduler;
  }
}
