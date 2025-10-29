package com.andyadc.test.http.claude;

public class HttpResult {

	private final int statusCode;
	private final String body;
	private final boolean success;
	private final String errorMessage;

	public HttpResult(int statusCode, String body, boolean success, String errorMessage) {
		this.statusCode = statusCode;
		this.body = body;
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public static HttpResult error(String errorMessage) {
		return new HttpResult(-1, "", false, errorMessage);
	}

	public boolean isSuccess() {
		return success && statusCode >= 200 && statusCode < 300;
	}

	public boolean isClientError() {
		return statusCode >= 400 && statusCode < 500;
	}

	public boolean isServerError() {
		return statusCode >= 500;
	}

	// getter方法
	public int getStatusCode() { return statusCode; }
	public String getBody() { return body; }
	public String getErrorMessage() { return errorMessage; }

	@Override
	public String toString() {
		return String.format("HttpResult{statusCode=%d, success=%s, bodyLength=%d}",
			statusCode, success, body != null ? body.length() : 0);
	}

}
