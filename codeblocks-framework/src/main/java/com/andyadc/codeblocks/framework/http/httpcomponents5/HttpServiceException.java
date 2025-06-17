package com.andyadc.codeblocks.framework.http.httpcomponents5;

import java.io.IOException;

public class HttpServiceException extends IOException {
	private static final long serialVersionUID = -3890918192239202578L;

	private final int statusCode;
	private final String responseBody;

	public HttpServiceException(String message, int statusCode, String responseBody) {
		super(message + " - Status: " + statusCode);
		this.statusCode = statusCode;
		this.responseBody = responseBody;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

}
