package com.andyadc.bms.common;

public enum UserStatus {
	INITIAL(0),
	NORMAL(1),
	FROZEN(2),
	;

	private final int status;

	UserStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
