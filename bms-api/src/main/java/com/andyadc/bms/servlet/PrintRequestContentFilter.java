package com.andyadc.bms.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@WebFilter(filterName = "printRequestContentFilter", urlPatterns = "/*")
public class PrintRequestContentFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(PrintRequestContentFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();
		logger.info("In PrintRequestContentFilter {}", uri);

		InputStream inputStream = request.getInputStream();
		byte[] body = StreamUtils.copyToByteArray(inputStream);
//		logger.info("In PrintRequestContentFilter. Request body is: {}", new String(body));

		filterChain.doFilter(request, response);
	}
}
