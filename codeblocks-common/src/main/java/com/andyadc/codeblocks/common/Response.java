package com.andyadc.codeblocks.common;

import java.io.Serializable;

/**
 * @author andy.an
 * @since 2018/3/25
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    public Response() {
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Response success() {
        return new Response(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage());
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

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message=" + message +
                "}";
    }
}
