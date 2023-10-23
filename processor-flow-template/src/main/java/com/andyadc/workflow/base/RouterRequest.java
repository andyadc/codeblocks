package com.andyadc.workflow.base;

public class RouterRequest {

	private String code;
	private String mode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int conditionSize() {
		int i = 0;
		if (this.code != null) {
			i++;
		}
		if (this.mode != null) {
			i++;
		}
		return i;
	}

}
