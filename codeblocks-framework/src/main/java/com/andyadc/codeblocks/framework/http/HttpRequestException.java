package com.andyadc.codeblocks.framework.http;

public class HttpRequestException extends RuntimeException {

	private static final long serialVersionUID = -4468068898952572673L;

	public HttpRequestException(Throwable cause) {
		super(cause);
	}

	public HttpRequestException(String message) {
		super(message);
	}

	public HttpRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
