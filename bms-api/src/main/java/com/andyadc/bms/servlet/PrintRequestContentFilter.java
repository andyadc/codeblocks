package com.andyadc.bms.servlet;

import com.andyadc.codeblocks.kit.idgen.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@WebFilter(filterName = "printRequestContentFilter", urlPatterns = "/*")
public class PrintRequestContentFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(PrintRequestContentFilter.class);

	private static final String TRACE_ID = "traceId";
	private static final String HEADER_TRACE_ID = "X_TRACE_ID";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println("IN PrintRequestContentFilter " + uri);
		String traceId = request.getHeader(HEADER_TRACE_ID);
		if (traceId == null || traceId.isEmpty()) {
			traceId = UUID.randomUUID();
		}
		MDC.put(TRACE_ID, traceId);
		Instant start = Instant.now();

		InputStream inputStream = request.getInputStream();
		byte[] body = StreamUtils.copyToByteArray(inputStream);
		System.out.println("In PrintRequestContentFilter. Request body is: " + new String(body));

		filterChain.doFilter(request, response);

		Instant end = Instant.now();
		logger.info("{} in {} ms", uri, Duration.between(start, end).toMillis());
	}

	@Override
	public void destroy() {
		super.destroy();
		MDC.clear();
		logger.info("Filter destroy");
	}
}
