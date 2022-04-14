package com.andyadc.bms.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class SecWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = -24026075116693364L;

	private final String verificationCode;

	public SecWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		verificationCode = request.getParameter("code");
	}

	public String getVerificationCode() {
		return verificationCode;
	}
}
