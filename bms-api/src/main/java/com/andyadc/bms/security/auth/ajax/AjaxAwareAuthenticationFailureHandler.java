package com.andyadc.bms.security.auth.ajax;

import com.andyadc.bms.common.RespCode;
import com.andyadc.bms.common.Response;
import com.andyadc.bms.security.exception.AuthMethodNotSupportedException;
import com.andyadc.bms.security.exception.JwtExpiredTokenException;
import com.andyadc.bms.security.handler.ResponseWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAwareAuthenticationFailureHandler extends ResponseWriter implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(AjaxAwareAuthenticationFailureHandler.class);

	private final ObjectMapper mapper;

	public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	protected Object body(HttpServletRequest request, Throwable e) {
		Response<Object> resp = Response.of(RespCode.UNAUTHORIZED);
		if (e instanceof BadCredentialsException) {
			resp.setMessage("Invalid username or password");
		} else if (e instanceof UsernameNotFoundException) {
			resp.setMessage("Invalid username or password");
		} else if (e instanceof JwtExpiredTokenException) {
			resp.setMessage("Token has expired");
		} else if (e instanceof AuthMethodNotSupportedException) {
			resp.setMessage(e.getMessage());
		} else {
			resp.setMessage("Authentication failed");
		}
		return resp;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

		this.write(request, response, e);

//		logger.error("AuthenticationFailure actual reason: ", e);
//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//		if (e instanceof BadCredentialsException) {
//			mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
//		} else if (e instanceof UsernameNotFoundException) {
//			mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
//		} else if (e instanceof JwtExpiredTokenException) {
//			mapper.writeValue(response.getWriter(), ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
//		} else if (e instanceof AuthMethodNotSupportedException) {
//			mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
//		} else {
//			mapper.writeValue(response.getWriter(), ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
//		}
	}
}
