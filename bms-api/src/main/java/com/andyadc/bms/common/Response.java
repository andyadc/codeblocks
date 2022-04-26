package com.andyadc.bms.common;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Response<T> {

	private String code;
	private String message;
	private Integer status;
	private String traceId;
	private T data;
	private final ZonedDateTime timestamp;

	public Response() {
		this.timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
		Object traceId = RequestContextHolder.currentRequestAttributes().getAttribute("traceId", RequestAttributes.SCOPE_REQUEST);
		if (traceId instanceof String) {
			setTraceId(Objects.toString(traceId));
		}
	}

	public Response(String code, String message) {
		this();
		this.code = code;
		this.message = message;
	}

	public Response(String code, String message, T data) {
		this(code, message);
		this.data = data;
	}

	public static <T> Response<T> of(String code, String message) {
		return new Response<>(code, message);
	}

	public static <T> Response<T> of(String code, String message, T t) {
		return new Response<>(code, message, t);
	}

	public static <T> Response<T> of(RespCode respCode) {
		return of(respCode.getCode(), respCode.getMessage());
	}

	public static <T> Response<T> of(RespCode respCode, T t) {
		return of(respCode.getCode(), respCode.getMessage(), t);
	}

	public static <T> Response<T> success(T t) {
		return of(RespCode.SUCC, t);
	}

	public static <T> Response<T> success() {
		return of(RespCode.SUCC);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

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

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Response.class.getSimpleName() + "[", "]")
			.add("code=" + code)
			.add("message=" + message)
			.add("status=" + status)
			.add("timestamp=" + timestamp)
			.add("traceId=" + traceId)
			.add("data=" + data)
			.toString();
	}
}
