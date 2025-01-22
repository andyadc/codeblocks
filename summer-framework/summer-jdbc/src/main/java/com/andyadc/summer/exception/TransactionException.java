package com.andyadc.summer.exception;

public class TransactionException extends DataAccessException {

	private static final long serialVersionUID = -1651656058575723273L;

	public TransactionException() {
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

}
