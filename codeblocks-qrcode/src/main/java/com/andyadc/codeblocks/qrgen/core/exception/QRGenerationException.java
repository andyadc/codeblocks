package com.andyadc.codeblocks.qrgen.core.exception;

public class QRGenerationException extends RuntimeException {
	public QRGenerationException(String message, Throwable underlyingException) {
		super(message, underlyingException);
	}
}
