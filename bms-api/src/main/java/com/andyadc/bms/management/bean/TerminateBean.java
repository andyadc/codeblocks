package com.andyadc.bms.management.bean;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;

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
