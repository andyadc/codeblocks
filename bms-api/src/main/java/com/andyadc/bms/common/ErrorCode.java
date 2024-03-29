package com.andyadc.bms.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of REST Error types.
 */
public enum ErrorCode {

	GLOBAL(2),
	AUTHENTICATION(10),
	JWT_TOKEN_EXPIRED(11);

	private final int errorCode;

	ErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@JsonValue
	public int getErrorCode() {
		return errorCode;
	}
}
