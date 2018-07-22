package com.andyadc.permission.exception;

/**
 * @author andaicheng
 * @since 2018/7/22
 */
public class PermissionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMessage;

    public PermissionException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public PermissionException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public PermissionException(String message, Throwable cause) {
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
