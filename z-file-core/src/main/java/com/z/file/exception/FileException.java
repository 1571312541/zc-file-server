package com.z.file.exception;

/**
 * FileStorage 运行时异常
 */
public class FileException extends RuntimeException {
    public FileException() {
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message,cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message,cause,enableSuppression,writableStackTrace);
    }
}
