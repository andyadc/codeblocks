package com.andyadc.security.extension.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleAccessDeniedHandler extends ResponseWriter implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		this.write(request, response);
	}

	@Override
	protected Map<String, Object> body(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<>(3);
		map.put("code", HttpStatus.FORBIDDEN.value());
		map.put("message", HttpStatus.FORBIDDEN.getReasonPhrase());
		map.put("uri", request.getRequestURI());
		return map;
	}
}
