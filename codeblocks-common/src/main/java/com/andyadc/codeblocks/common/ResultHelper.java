package com.andyadc.codeblocks.common;

public final class ResultHelper {

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.setCode(ResultCodeMessage.SUCCESS.code());
		result.setMessage(ResultCodeMessage.SUCCESS.message());
		result.setData(data);
		result.setTimestamp(System.currentTimeMillis());
		return result;
	}

	public static <T> Result<T> fail(String message) {
		Result<T> result = new Result<>();
		result.setCode(ResultCodeMessage.FAILURE.code());
		result.setMessage(message);
		result.setTimestamp(System.currentTimeMillis());
		return result;
	}

	public static <T> Result<T> fail(String code, String message) {
		Result<T> result = new Result<>();
		result.setCode(code);
		result.setMessage(message);
		result.setTimestamp(System.currentTimeMillis());
		return result;
	}
}
