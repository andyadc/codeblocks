package com.andyadc.codeblocks.framework.http;

public class HttpRequestException extends RuntimeException {
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
