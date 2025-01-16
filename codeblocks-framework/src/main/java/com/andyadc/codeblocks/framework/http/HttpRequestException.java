package com.andyadc.codeblocks.framework.http;

public class HttpRequestException extends RuntimeException {

	private static final long serialVersionUID = -4468068898952572673L;

	private int statusCode;

	public HttpRequestException(Throwable cause) {
		super(cause);
	}

	public HttpRequestException(String message) {
		super(message);
	}

	public HttpRequestException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public HttpRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getStatusCode() {
		return statusCode;
	}

}
