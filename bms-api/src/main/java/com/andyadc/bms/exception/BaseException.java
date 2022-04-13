package com.andyadc.bms.exception;

/**
 * Base Exception is the parent of all exceptions
 */
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 8270866309404469476L;
	private String errCode;

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String errCode, String message) {
		super(message);
		this.errCode = errCode;
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String errCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
}
