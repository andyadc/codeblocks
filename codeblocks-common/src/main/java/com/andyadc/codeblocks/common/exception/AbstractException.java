package com.andyadc.codeblocks.common.exception;

import com.andyadc.codeblocks.common.ResultCodeMessage;

import java.util.Optional;

public abstract class AbstractException extends RuntimeException {

	private static final long serialVersionUID = 1220908466769567005L;

	private final String code;
	private final String message;

	public AbstractException(ResultCodeMessage codeMessage, String message, Throwable cause) {
		super(message, cause);
		this.code = codeMessage.code();
		this.message = Optional.ofNullable(message).orElse(codeMessage.message());
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
