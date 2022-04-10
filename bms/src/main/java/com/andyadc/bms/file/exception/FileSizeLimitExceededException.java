package com.andyadc.bms.file.exception;

import com.andyadc.bms.exception.BaseException;

public class FileSizeLimitExceededException extends BaseException {
	private static final long serialVersionUID = 3989346896586424359L;

	public FileSizeLimitExceededException(String message) {
		super(message);
	}
}
