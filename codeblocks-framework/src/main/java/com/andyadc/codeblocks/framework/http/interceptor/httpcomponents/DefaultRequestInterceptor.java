package com.andyadc.codeblocks.framework.http.interceptor.httpcomponents;

import com.andyadc.codeblocks.common.constants.Constants;
import com.andyadc.codeblocks.kit.idgen.UUID;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DefaultRequestInterceptor implements HttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultRequestInterceptor.class);

	@Override
	public void process(HttpRequest request, HttpContext context) {
		context.setAttribute("requestId", UUID.randomUUID());
		context.setAttribute("startingAt", System.nanoTime());
		logger.info("RequestLine {}", request.getRequestLine());

		String traceId = MDC.get(Constants.TRACE_ID);
		if (traceId != null) {
			request.addHeader(Constants.TRACE_ID, traceId);
		}
	}
}
