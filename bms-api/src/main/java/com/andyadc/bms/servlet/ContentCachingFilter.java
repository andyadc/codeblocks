package com.andyadc.bms.servlet;

import com.andyadc.bms.modules.log.service.LogRecordService;
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

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "contentCachingFilter", urlPatterns = "/*")
public class ContentCachingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ContentCachingFilter.class);

	private static final String TRACE_ID = "traceId";
	private static final String HEADER_REQUEST_ID = "X-Request-Id";

	private LogRecordService logRecordService;

	@Inject
	public void setLogRecordService(LogRecordService logRecordService) {
		this.logRecordService = logRecordService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		System.out.println("IN ContentCachingFilter");

		logRecordService.saveReqeustLog(request);

		String traceId = request.getHeader(HEADER_REQUEST_ID);
		if (traceId == null || traceId.isEmpty()) {
			traceId = UUID.randomUUID().replace("-", "");
		}
		MDC.put(TRACE_ID, traceId);
		RequestContextHolder.currentRequestAttributes().setAttribute("traceId", traceId, RequestAttributes.SCOPE_REQUEST);
		String uri = request.getRequestURI();

		Instant start = Instant.now();

		CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);

		setHeader(response, traceId);
		filterChain.doFilter(cachedBodyHttpServletRequest, response);

		Instant end = Instant.now();

		logger.info("{} {} in {} ms", request.getMethod(), uri, Duration.between(start, end).toMillis());
		RequestContextHolder.resetRequestAttributes();
		MDC.clear();
	}

	private void setHeader(HttpServletResponse response, String traceId) {
		response.setHeader("Date", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));
		response.setHeader("X-Request-Id", traceId);
		response.setHeader("X-Frame-Options", "deny");
		response.setHeader("X-XSS-Protection", "0");
		response.setHeader("X-Content-Type-Options", "nosniff");
//		response.setHeader("Content-Security-Policy", "default-src 'none'; script-src 'self'; connect-src 'self'; img-src 'self'; style-src 'self'; frame-ancestors 'self'; form-action 'self';");
		response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
	}

	@Override
	public void destroy() {
		super.destroy();
		logger.info("Filter destroy");
	}
}
