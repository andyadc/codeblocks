package com.andyadc.bms.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtGenException extends AuthenticationException {

	public JwtGenException(String msg) {
		super(msg);
	}
}
