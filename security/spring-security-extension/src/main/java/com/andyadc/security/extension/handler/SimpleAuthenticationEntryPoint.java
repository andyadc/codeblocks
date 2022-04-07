package com.andyadc.security.extension.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleAuthenticationEntryPoint extends ResponseWriter implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		String message = exceptionMessage(authException);
		request.setAttribute("exMessage", message);
		this.write(request, response);
	}

	@Override
	protected Map<String, Object> body(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<>(3);
		String exMessage = (String) request.getAttribute("exMessage");
		map.put("code", HttpStatus.UNAUTHORIZED.value());
		map.put("message", exMessage);
		map.put("uri", request.getRequestURI());
		return map;
	}

	private String exceptionMessage(AuthenticationException exception) {
		String message = "访问未授权";
		if (exception instanceof AccountExpiredException) {
			message = "账户过期";
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			message = "用户身份凭证未找到";
		} else if (exception instanceof AuthenticationServiceException) {
			message = "用户身份认证服务异常";
		} else if (exception instanceof BadCredentialsException) {
			message = exception.getMessage();
		}
		return message;
	}
}
