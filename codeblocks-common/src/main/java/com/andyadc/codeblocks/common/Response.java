package com.andyadc.codeblocks.common;

import com.andyadc.codeblocks.common.enums.StatusCode;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private T data;

    public Response() {
    }

	public Response(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public Response(T data) {
		this.data = data;
	}

	public static Response<?> success() {
		return new Response<>(StatusCode.SUCCESS.code(), StatusCode.SUCCESS.message());
	}

	public static <T> Response<?> success(T t) {
		Response<T> response = new Response<>(t);
		response.setCode(StatusCode.SUCCESS.code());
		response.setMessage(StatusCode.SUCCESS.message());
		return response;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response {" +
                "code=" + code +
                ", message=" + message +
                ", data=" + data +
                '}';
    }
}
