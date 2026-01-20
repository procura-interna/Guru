package pt.procurainterna.guru.quartz;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import pt.procurainterna.guru.cdi.CDIProvider;

public final class CdiJobFactory implements JobFactory {
  private final CDIProvider cdi;

  public CdiJobFactory(CDIProvider cdi) {
    this.cdi = cdi;
  }

  @Override
  public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
    Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
    return cdi.getInstance(jobClass); // your provider must support getInstance(Class<T>)
  }
}
