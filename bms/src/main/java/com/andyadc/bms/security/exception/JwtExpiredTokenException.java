package com.andyadc.bms.security.exception;

import com.andyadc.bms.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {

	private static final long serialVersionUID = 8534968461091794799L;
	private JwtToken token;

	public JwtExpiredTokenException(String msg) {
		super(msg);
	}

	public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
		super(msg, t);
		this.token = token;
	}

	public String token() {
		return this.token.getToken();
	}
}
