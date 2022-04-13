package com.andyadc.bms.security.exception;

import com.andyadc.bms.exception.BaseException;

public class RequestException extends BaseException {
	private static final long serialVersionUID = 8618299593586140232L;

	public RequestException(String message) {
		super(message);
	}

	public RequestException(String errCode, String message, Throwable cause) {
		super(errCode, message, cause);
	}
}
