package com.andyadc.summer.exception;

public class DataAccessException extends NestedRuntimeException {

	private static final long serialVersionUID = -9085349837762233988L;

	public DataAccessException() {
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
