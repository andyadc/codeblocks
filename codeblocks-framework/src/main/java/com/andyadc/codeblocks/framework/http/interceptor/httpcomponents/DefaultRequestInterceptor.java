package com.andyadc.codeblocks.framework.http.interceptor.httpcomponents;

import com.andyadc.codeblocks.kit.idgen.UUID;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRequestInterceptor implements HttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultRequestInterceptor.class);

	@Override
	public void process(HttpRequest request, HttpContext context) {
		context.setAttribute("requestId", UUID.randomUUID());
		context.setAttribute("startingAt", System.nanoTime());
		logger.info("RequestLine {}", request.getRequestLine());
	}
}
