package com.andyadc.bms.security.auth.ajax;

import com.andyadc.bms.security.exception.AuthMethodNotSupportedException;
import com.andyadc.bms.security.model.LoginRequest;
import com.andyadc.bms.utils.WebUtil;
import com.andyadc.codeblocks.kit.net.IPUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(AjaxLoginProcessingFilter.class);

	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;

	private final ObjectMapper objectMapper;

	public AjaxLoginProcessingFilter(String defaultFilterProcessesUrl,
									 AuthenticationSuccessHandler successHandler,
									 AuthenticationFailureHandler failureHandler,
									 ObjectMapper objectMapper) {
		super(defaultFilterProcessesUrl);
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
			logger.warn("Authentication request method not supported. Request method: " + request.getMethod());
			throw new AuthMethodNotSupportedException("Authentication method not supported");
		}
		String ip = IPUtil.getRemoteIp(request);
		if (!WebUtil.isAjax(request)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication request not Ajax");
			}
			//TODO
		}

		LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		logger.info("Login username: [{}], IP: {}", username, ip);
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			String requestStr = IOUtils.toString(request.getReader());
			logger.warn("Login request info: {}", requestStr);
			throw new AuthenticationServiceException("Username or Password not provided");
		}

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		successHandler.onAuthenticationSuccess(request, response, authentication);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
}
