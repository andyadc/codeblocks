package com.andyadc.codeblocks.framework.http.interceptor;

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
public class DefaultHttpResponseInterceptor implements HttpResponseInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultHttpResponseInterceptor.class);

	@Override
	public void process(HttpResponse httpResponse, HttpContext httpContext) {
		long startingAt = (long) httpContext.getAttribute("startingAt");
		logger.info("Request timing={}", Instant.now().toEpochMilli() - startingAt);
	}
}
