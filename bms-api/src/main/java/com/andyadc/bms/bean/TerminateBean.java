package com.andyadc.bms.bean;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

public class TerminateBean {

	@Inject
	ApplicationAvailability availability;

	@PreDestroy
	public void onDestroy() throws Exception {
		LivenessState livenessState = availability.getLivenessState();
		System.out.println(livenessState);

		ReadinessState readinessState = availability.getReadinessState();
		System.out.println(readinessState);

		System.out.println("Spring Container is destroyed!");
	}
}
