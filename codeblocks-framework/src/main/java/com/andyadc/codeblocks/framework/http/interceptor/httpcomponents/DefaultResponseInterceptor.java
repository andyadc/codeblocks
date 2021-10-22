package com.andyadc.codeblocks.framework.http.interceptor.httpcomponents;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultResponseInterceptor implements HttpResponseInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultResponseInterceptor.class);

	@Override
	public void process(HttpResponse response, HttpContext context) {
		long startingAt = (long) context.getAttribute("startingAt");
		logger.info(String.format("Received response in %.1fms %s",
			(System.nanoTime() - startingAt) / 1e6d,
			response.getStatusLine()
			)
		);
	}
}
