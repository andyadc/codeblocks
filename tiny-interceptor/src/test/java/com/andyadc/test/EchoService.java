package com.andyadc.test;

/**
 * Echo Service
 */
public class EchoService {
	@Logging
	public String echo(String message) {
		return "[ECHO] : " + message;
	}
}
