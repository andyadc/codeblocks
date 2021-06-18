package com.andyadc.tinyrpc;

import java.io.Serializable;

public class InvocationResponse implements Serializable {

	private String requestId;
	private String errorMessage;
	private Object entity;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}
}
