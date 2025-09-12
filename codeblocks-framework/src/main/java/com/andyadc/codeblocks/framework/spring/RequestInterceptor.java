package com.andyadc.codeblocks.framework.spring;

import com.andyadc.codeblocks.kit.idgen.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

public class RequestInterceptor implements AsyncHandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

	private final static String HTTP_HEADER_TRACE_ID = "X-TraceId";
	private final static String TRACE_ID = "traceId";
	private static final ThreadLocal<Long> requestTimeCounterThreadLocal = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		requestTimeCounterThreadLocal.set(System.currentTimeMillis());
		String traceId = request.getHeader(HTTP_HEADER_TRACE_ID);
		if (traceId == null || traceId.isEmpty()) {
			traceId = UUID.randomUUID();
		}
		MDC.put(TRACE_ID, traceId);
		if (logger.isDebugEnabled()) {
			logger.debug(">>> {}", request.getRequestURI());
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (logger.isDebugEnabled()) {
			logger.info("Request elapsed: {}", System.currentTimeMillis() - requestTimeCounterThreadLocal.get());
		}
		MDC.remove(TRACE_ID);
		requestTimeCounterThreadLocal.remove();
	}
}
