package com.andyadc.workflow.service.dto;

import java.util.HashMap;
import java.util.Map;

public class OrderProcessResponse {

	private String requestId;
	private String resultCode;
	private String errorMessage;
	private Map<String, String> data = new HashMap<>();

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "OrderProcessResponse{" +
			"requestId=" + requestId +
			", resultCode=" + resultCode +
			", errorMessage=" + errorMessage +
			", data=" + data +
			'}';
	}
}
