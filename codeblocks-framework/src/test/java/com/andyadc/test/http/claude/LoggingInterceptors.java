package com.andyadc.test.http.claude;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoggingInterceptors {

	public static class RequestLoggingInterceptor implements HttpRequestInterceptor {

		private static final Logger logger = LoggerFactory.getLogger("HTTP_REQUEST");

		@Override
		public void process(HttpRequest request, HttpContext context) {

			context.setAttribute("startingAt", System.nanoTime());

			logger.info(">>> Request: {} {}",
				request.getRequestLine().getMethod(),
				request.getRequestLine().getUri());

			for (Header header : request.getAllHeaders()) {
				logger.debug(">>> Header: {}: {}", header.getName(), header.getValue());
			}
		}
	}

	public static class ResponseLoggingInterceptor implements HttpResponseInterceptor {

		private static final Logger logger = LoggerFactory.getLogger("HTTP_RESPONSE");

		@Override
		public void process(HttpResponse response, HttpContext context) {

			Object startingAt = context.getAttribute("startingAt");
			if (startingAt != null) {
				long starting = (long) startingAt;
				logger.info("Request completed in {}", (System.nanoTime()-starting)/1_000_000);
			}

			logger.info("<<< Response: {}",
				response.getStatusLine());

			for (Header header : response.getAllHeaders()) {
				logger.debug("<<< Header: {}: {}", header.getName(), header.getValue());
			}
		}
	}

}
