package com.andyadc.bms.security.auth.ajax;

import com.andyadc.bms.common.ErrorCode;
import com.andyadc.bms.common.ErrorResponse;
import com.andyadc.bms.security.exception.AuthMethodNotSupportedException;
import com.andyadc.bms.security.exception.JwtExpiredTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(AjaxAwareAuthenticationFailureHandler.class);

	private final ObjectMapper mapper;

	@Inject
	public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		logger.error("AuthenticationFailure actual reason: ", e);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		if (e instanceof BadCredentialsException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof UsernameNotFoundException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof JwtExpiredTokenException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof AuthMethodNotSupportedException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		}
	}
}
