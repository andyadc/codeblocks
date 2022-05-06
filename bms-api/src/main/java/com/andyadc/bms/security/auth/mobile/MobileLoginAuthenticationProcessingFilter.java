package com.andyadc.bms.security.auth.mobile;

import com.andyadc.bms.security.exception.AuthMethodNotSupportedException;
import com.andyadc.bms.security.model.LoginRequest;
import com.andyadc.codeblocks.kit.mask.MaskType;
import com.andyadc.codeblocks.kit.net.IPUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MobileLoginAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(MobileLoginAuthenticationProcessingFilter.class);

	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;

	private final ObjectMapper objectMapper;

	public MobileLoginAuthenticationProcessingFilter(String defaultFilterProcessesUrl,
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

		LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
		String phoneNo = loginRequest.getPhoneNo();
		String verificationCode = loginRequest.getVerificationCode();

		logger.info("Login phoneNo: [{}], IP: {}", MaskType.MOBILE.mask(phoneNo), ip);
		if (StringUtils.isBlank(phoneNo) || StringUtils.isBlank(verificationCode)) {
			String requestStr = IOUtils.toString(request.getReader());
			logger.warn("Login request info: {}", requestStr);
			throw new AuthenticationServiceException("phoneNo or verificationCode not provided");
		}

		MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(phoneNo, verificationCode);
		return this.getAuthenticationManager().authenticate(authenticationToken);
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
