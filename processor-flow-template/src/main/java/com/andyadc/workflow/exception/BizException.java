package com.andyadc.workflow.exception;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = -6885790704190148108L;

	private String code;
	private String message;

	public BizException() {
	}

	public BizException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
