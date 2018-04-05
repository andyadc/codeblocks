package com.andyadc.codeblocks.common;

/**
 * @author andy.an
 * @since 2018/3/27
 */
public class ErrorResponse extends Response {

    private static final long serialVersionUID = 1L;

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error=" + error +
                "} " + super.toString();
    }
}
