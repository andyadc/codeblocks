package com.andyadc.codeblocks.serialization.compression;

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
