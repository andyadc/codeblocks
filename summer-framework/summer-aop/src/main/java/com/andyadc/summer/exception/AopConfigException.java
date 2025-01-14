package com.andyadc.summer.exception;

public class AopConfigException extends NestedRuntimeException {

	private static final long serialVersionUID = -5952440358365047677L;

	public AopConfigException() {
		super();
	}

	public AopConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public AopConfigException(String message) {
		super(message);
	}

	public AopConfigException(Throwable cause) {
		super(cause);
	}

}
