package com.andyadc.codeblocks.framework.http.interceptor;

import com.andyadc.codeblocks.kit.idgen.UUID;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.time.Instant;

/**
 * andy.an
 * 2019/12/9
 */
public class DefaultRequestInterceptor implements HttpRequestInterceptor {

	@Override
	public void process(HttpRequest httpRequest, HttpContext httpContext) {
		httpContext.setAttribute("requestId", UUID.randomUUID());
		httpContext.setAttribute("startingAt", Instant.now().toEpochMilli());
	}
}
