package com.andyadc.bms.security.exception;

import com.andyadc.bms.exception.BaseException;

public class MessageSendException extends BaseException {

	private static final long serialVersionUID = 8675504023384830201L;

	public MessageSendException(String message) {
		super(message);
	}
}
