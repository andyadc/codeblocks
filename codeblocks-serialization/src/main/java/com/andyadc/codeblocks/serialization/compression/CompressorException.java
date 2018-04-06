package com.andyadc.codeblocks.serialization.compression;

/**
 * @author andaicheng
 * @version 2017/1/9
 */
public class CompressorException extends RuntimeException {

    public CompressorException() {
        super();
    }

    public CompressorException(String message) {
        super(message);
    }

    public CompressorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompressorException(Throwable cause) {
        super(cause);
    }
}
