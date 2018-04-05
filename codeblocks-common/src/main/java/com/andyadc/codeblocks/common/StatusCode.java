package com.andyadc.codeblocks.common;

/**
 * @author andaicheng
 * @since 2018/4/5
 */
public enum StatusCode {

    SUCCESS("000", "成功"),
    FAILED("400", "失败");

    private String code;
    private String message;

    StatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
