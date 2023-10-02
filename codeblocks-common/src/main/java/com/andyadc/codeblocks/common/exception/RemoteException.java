package com.andyadc.codeblocks.common.exception;

import com.andyadc.codeblocks.common.ResultCodeMessage;

public class RemoteException extends AbstractException {

	private static final long serialVersionUID = -6182195919613701611L;

	public RemoteException() {
		super(ResultCodeMessage.REMOTE_ERROR, null, null);
	}

	public RemoteException(String message) {
		super(ResultCodeMessage.REMOTE_ERROR, message, null);
	}
}
