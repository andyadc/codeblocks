package com.andyadc.bms.config.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;

/**
 * https://www.baeldung.com/spring-resttemplate-logging
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] reqbody, ClientHttpRequestExecution execution) throws IOException {
		URI uri = request.getURI();
		Instant start = Instant.now();
		ClientHttpResponse response = execution.execute(request, reqbody);
		Instant end = Instant.now();
		logger.info("Request {} {} Response {} elapsed time={}",
			uri.getScheme(), uri.toString(),
			response.getStatusCode(),
			Duration.between(start, end).toMillis());
		return response;
	}
}
