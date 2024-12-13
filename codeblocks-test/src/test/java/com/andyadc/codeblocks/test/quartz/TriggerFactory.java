package com.andyadc.codeblocks.test.quartz;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.HashMap;
import java.util.Map;

public class TriggerFactory {

	public Trigger createSimpleTrigger(String cronExpression) throws Exception {

		SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
		simpleTriggerFactoryBean.setName("");
		simpleTriggerFactoryBean.setRepeatInterval(10L);

		return simpleTriggerFactoryBean.getObject();
	}

	public Trigger createCronTrigger(String cronExpression) throws Exception {

		HelloService helloService = new HelloService();

		MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		methodInvokingJobDetailFactoryBean.setTargetObject(helloService);
		methodInvokingJobDetailFactoryBean.setTargetMethod("execute");
		methodInvokingJobDetailFactoryBean.setConcurrent(false);
		methodInvokingJobDetailFactoryBean.setName("Job-");
		methodInvokingJobDetailFactoryBean.afterPropertiesSet();
		JobDetail jobDetail = methodInvokingJobDetailFactoryBean.getObject();

		assert jobDetail != null;
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(cronExpression);
		cronTriggerFactoryBean.setName("Trigger-");

		Map<String, Object> jobDataAsMap = new HashMap<>();
		jobDataAsMap.put("warnEmail", "@");
		cronTriggerFactoryBean.setJobDataAsMap(jobDataAsMap);

		cronTriggerFactoryBean.afterPropertiesSet();

		return cronTriggerFactoryBean.getObject();
	}

}
