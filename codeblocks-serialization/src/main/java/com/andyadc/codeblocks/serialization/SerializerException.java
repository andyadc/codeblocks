package com.andyadc.codeblocks.serialization;

/**
 * @author andaicheng
 * @version 2016/12/30
 */
public class SerializerException extends RuntimeException {

    public SerializerException() {
        super();
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }
}
