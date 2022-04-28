package com.andyadc.bms.common;

public enum RespCode {

	SUCC("000", "Success"),
	UNKNOWN("-1", "Unknown error"),
	PENDING("999", "Please wait"),
	Method_Not_Allowed("405", "Method Not Allowed"),
	UNAUTHORIZED("401", "Unauthorized"),
	AUTHENTICATION_FAIL("010", "Invalid username or password"),
	ILLEGAL_PASSWORD("011", "Illegal password"),
	JWT_TOKEN_EXPIRED("020", "Token has expired"),
	PERMISSION_MISSED("030", "User doesn't have any privileges"),
	VALIDATE_CODE_ERROR("050", "Validate code error"),
	;

	private final String code;
	private final String message;

	RespCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
