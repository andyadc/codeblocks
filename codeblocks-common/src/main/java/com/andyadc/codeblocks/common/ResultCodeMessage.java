package com.andyadc.codeblocks.common;

public enum ResultCodeMessage {

	SUCCESS("000", "Success"),
	FAILURE("999", "Failure"),
	REMOTE_ERROR("999", "remote error!"),
	;

	private final String code;
	private final String message;

	ResultCodeMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
}
