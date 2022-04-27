package com.andyadc.boot.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] reqbody, ClientHttpRequestExecution execution) throws IOException {
		logger.debug("Request body: {}, uri: {}", new String(reqbody, StandardCharsets.UTF_8), request.getURI().toString());
		System.out.println(">>>" + request.getMethodValue());
		System.out.println(">>>" + request.getURI().getScheme());
		Instant start = Instant.now();
		ClientHttpResponse response = execution.execute(request, reqbody);
		Instant end = Instant.now();
		System.out.println(response.getRawStatusCode());
		System.out.println(response.getStatusCode());
		InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
		String respbody = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
		logger.debug("Response body: {}, elapsed time={}", respbody, Duration.between(start, end).toMillis());
		return response;
	}
}
