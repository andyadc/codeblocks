package com.andyadc.codeblocks.common.enums;

public enum StatusCode {

	SUCCESS("000", "成功"),
	FAILED("400", "失败"),
	;

	private final String code;
	private final String message;

	StatusCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

    public String message() {
        return message;
    }
}
