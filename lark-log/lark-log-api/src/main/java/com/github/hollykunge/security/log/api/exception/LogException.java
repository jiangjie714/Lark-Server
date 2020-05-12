package com.github.hollykunge.security.log.api.exception;

public class LogException extends RuntimeException {
    private int status = 500;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LogException() {
    }

    public LogException(String message, int status) {
        super(message);
        this.status = status;
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }

    public LogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
