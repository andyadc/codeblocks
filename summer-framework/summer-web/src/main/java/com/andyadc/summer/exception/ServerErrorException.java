package com.andyadc.summer.exception;

public class ServerErrorException extends ErrorResponseException {

	private static final long serialVersionUID = -7160154309139019533L;

	public ServerErrorException() {
		super(500);
	}

	public ServerErrorException(String message) {
		super(500, message);
	}

	public ServerErrorException(Throwable cause) {
		super(500, cause);
	}

	public ServerErrorException(String message, Throwable cause) {
		super(500, message, cause);
	}

}
