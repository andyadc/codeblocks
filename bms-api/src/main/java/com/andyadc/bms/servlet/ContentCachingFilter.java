package com.andyadc.bms.servlet;

import com.andyadc.codeblocks.kit.idgen.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "contentCachingFilter", urlPatterns = "/*")
public class ContentCachingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ContentCachingFilter.class);

	private static final String TRACE_ID = "traceId";
	private static final String HEADER_TRACE_ID = "X_TRACE_ID";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		System.out.println("IN ContentCachingFilter");
		String traceId = request.getHeader(HEADER_TRACE_ID);
		if (traceId == null || traceId.isEmpty()) {
			traceId = UUID.randomUUID();
		}
		MDC.put(TRACE_ID, traceId);
		RequestContextHolder.currentRequestAttributes().setAttribute("traceId", traceId, RequestAttributes.SCOPE_REQUEST);
		String uri = request.getRequestURI();

		Instant start = Instant.now();

		CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
		filterChain.doFilter(cachedBodyHttpServletRequest, response);

		Instant end = Instant.now();

		logger.info("{} {} in {} ms", request.getMethod(), uri, Duration.between(start, end).toMillis());
		RequestContextHolder.resetRequestAttributes();
		MDC.clear();
	}

	@Override
	public void destroy() {
		super.destroy();
		logger.info("Filter destroy");
	}
}
