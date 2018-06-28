package com.andyadc.codeblocks.qrcode;

/**
 * @author andy.an
 * @since 2018/6/28
 */
public class ZxingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ZxingException() {
        super();
    }

    public ZxingException(String message) {
        super(message);
    }

    public ZxingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZxingException(Throwable cause) {
        super(cause);
    }
}
