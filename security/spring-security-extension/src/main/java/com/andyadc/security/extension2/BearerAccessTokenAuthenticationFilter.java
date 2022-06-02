package com.andyadc.security.extension2;

import com.andyadc.security.extension2.handler.SimpleAuthenticationEntryPoint;
import com.andyadc.security.extension2.jwt.JwtTokenStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BearerAccessTokenAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(BearerAccessTokenAuthenticationFilter.class);

	private static final String AUTHENTICATION_PREFIX = "Bearer ";
	private final JwtDecoder jwtDecoder;
	private final JwtTokenStorage jwtTokenStorage;
	private final AuthenticationEntryPoint authenticationEntryPoint = new SimpleAuthenticationEntryPoint();

	/**
	 * Instantiates a new Bearer token authentication filter.
	 *
	 * @param jwtDecoder      the jwt decoder
	 * @param jwtTokenStorage the jwt token storage
	 */
	public BearerAccessTokenAuthenticationFilter(JwtDecoder jwtDecoder, JwtTokenStorage jwtTokenStorage) {
		this.jwtDecoder = jwtDecoder;
		this.jwtTokenStorage = jwtTokenStorage;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		if (response.isCommitted()) {
			logger.debug("Response has already been committed");
			return;
		}
		// 如果已经通过认证
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			chain.doFilter(request, response);
			return;
		}
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(header) && header.startsWith(AUTHENTICATION_PREFIX)) {
			String jwtToken = header.replace(AUTHENTICATION_PREFIX, "");

			return;
		}
		chain.doFilter(request, response);
	}
}
