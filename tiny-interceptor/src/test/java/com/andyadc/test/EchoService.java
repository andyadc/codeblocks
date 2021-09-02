package com.andyadc.test;

import javax.annotation.PostConstruct;

/**
 * Echo Service
 */
public class EchoService {

	@Logging
	public String echo(String message) {
		return "[ECHO] : " + message;
	}

	@PostConstruct
	public void init() {
		System.out.println("Initializing...");
	}
}
