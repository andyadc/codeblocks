package com.andyadc.codeblocks.framework.rabbitmq;

public class ChannelException extends RuntimeException {

	private static final long serialVersionUID = 9220799651524428636L;

	public ChannelException(String message, Throwable cause) {
		super(message, cause);
	}
}
