package com.andyadc.security.extension.handler;

import com.andyadc.security.extension.JwtTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginAuthenticationSuccessHandler extends ResponseWriter implements AuthenticationSuccessHandler {

	private final JwtTokenGenerator jwtTokenGenerator;

	public LoginAuthenticationSuccessHandler(JwtTokenGenerator jwtTokenGenerator) {
		this.jwtTokenGenerator = jwtTokenGenerator;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		request.setAttribute("authentication", authentication);
		this.write(request, response);
	}

	@Override
	protected Map<String, Object> body(HttpServletRequest request) {
		Authentication authentication = (Authentication) request.getAttribute("authentication");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Map<String, Object> map = new LinkedHashMap<>(3);
		map.put("code", HttpStatus.OK.value());
		map.put("message", HttpStatus.OK.getReasonPhrase());
		map.put("data", jwtTokenGenerator.token(userDetails));
		return map;
	}
}
