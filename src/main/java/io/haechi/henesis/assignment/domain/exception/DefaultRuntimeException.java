package io.haechi.henesis.assignment.domain.exception;

public class DefaultRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public DefaultRuntimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public DefaultRuntimeException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DefaultRuntimeException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DefaultRuntimeException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public DefaultRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
