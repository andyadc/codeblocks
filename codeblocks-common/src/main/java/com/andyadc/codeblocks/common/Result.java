package com.andyadc.codeblocks.common;

import java.io.Serializable;

public class Result<T> implements Serializable {

	private static final long serialVersionUID = -6091198082203469296L;

	private String code;
	private String message;
	private T data;
	private long timestamp;
	private String traceId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	@Override
	public String toString() {
		return "Result{" +
			"code=" + code +
			", message=" + message +
			", data=" + data +
			", timestamp=" + timestamp +
			", traceId=" + traceId +
			'}';
	}
}
