package com.andyadc.bms.security.exception;

import com.andyadc.bms.exception.BaseException;

public class InvalidJwtTokenException extends BaseException {
	private static final long serialVersionUID = -3219081698001591619L;

	public InvalidJwtTokenException(String message) {
		super(message);
	}

	public InvalidJwtTokenException(String errCode, String message) {
		super(errCode, message);
	}
}
