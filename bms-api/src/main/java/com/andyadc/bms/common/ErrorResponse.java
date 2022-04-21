package com.andyadc.bms.common;

import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Error model for interacting with client.
 */
public class ErrorResponse {

	// timestamp
	private final ZonedDateTime timestamp;
	// HTTP Response Status Code
	private HttpStatus status;
	// General Error message
	private String message;
	// Error code
	private ErrorCode errorCode;
	private String path;

	public ErrorResponse() {
		this.timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
	}

	protected ErrorResponse(final String message, final ErrorCode errorCode, HttpStatus status) {
		this();
		this.message = message;
		this.errorCode = errorCode;
		this.status = status;
	}

	public static ErrorResponse of(final String message, final ErrorCode errorCode, HttpStatus status) {
		return new ErrorResponse(message, errorCode, status);
	}

	public Integer getStatus() {
		return status.value();
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}
}
