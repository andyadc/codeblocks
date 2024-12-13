package com.andyadc.codeblocks.test.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTests {

	public static void main(String[] args) throws Exception {

		// Grab the Scheduler instance from the Factory
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		// and start it off
		scheduler.start();

		// define the job and tie it to our HelloJob class
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
			.withIdentity("helloJob", "group")
			.build();

		// Trigger the job to run now, and then repeat every 10 seconds
		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("helloTrigger", "group")
			.forJob(jobDetail)
			.withSchedule(
				SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(10)
					.repeatForever()
			)
			.build();

		scheduler.scheduleJob(jobDetail, trigger);

//		scheduler.shutdown();
	}

	@Test
	public void test() {

	}

}
