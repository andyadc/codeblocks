package com.andyadc.codeblocks.framework.http.interceptor.httpcomponents;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * andy.an
 * 2019/12/9
 */
public class DefaultResponseInterceptor implements HttpResponseInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultResponseInterceptor.class);

	@Override
	public void process(HttpResponse response, HttpContext context) {
		long startingAt = (long) context.getAttribute("startingAt");
		logger.info("Request elapsed time={}", Instant.now().toEpochMilli() - startingAt);
	}
}
