package com.andyadc.workflow.base;

import java.io.Serializable;

public class RouterRequest implements Serializable {

	private static final long serialVersionUID = 4079847171499158803L;

	private String functionCode;
	private String payMode;

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public int conditionSize() {
		int i = 0;
		if (this.functionCode != null) {
			i++;
		}
		if (this.payMode != null) {
			i++;
		}
		return i;
	}

	@Override
	public String toString() {
		return "RouterRequest{" +
			"functionCode=" + functionCode +
			", payMode=" + payMode +
			'}';
	}
}
