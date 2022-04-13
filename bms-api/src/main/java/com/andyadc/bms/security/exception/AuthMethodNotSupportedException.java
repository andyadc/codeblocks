package com.andyadc.bms.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthMethodNotSupportedException extends AuthenticationServiceException {
	private static final long serialVersionUID = -8257174473683414658L;

	public AuthMethodNotSupportedException(String msg) {
		super(msg);
	}
}
