package com.andyadc.bms.management.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements AsyncHandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

	private static final ThreadLocal<Long> requestTimeCounterThreadLocal = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		requestTimeCounterThreadLocal.set(System.currentTimeMillis());
		if (logger.isDebugEnabled()) {
			logger.debug(">>> " + request.getRequestURI());
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (logger.isDebugEnabled()) {
			logger.info("Request elapsed: " + (System.currentTimeMillis() - requestTimeCounterThreadLocal.get()));
		}
		requestTimeCounterThreadLocal.remove();
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterConcurrentHandlingStarted");
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("postHandle");
		}
	}
}
