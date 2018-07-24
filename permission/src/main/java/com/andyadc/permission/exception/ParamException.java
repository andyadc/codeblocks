package com.andyadc.permission.exception;

/**
 * @author andy.an
 * @since 2018/7/24
 */
public class ParamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMessage;

    public ParamException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public ParamException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
