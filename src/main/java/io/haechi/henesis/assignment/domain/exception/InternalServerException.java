package io.haechi.henesis.assignment.domain.exception;

public class InternalServerException extends DefaultRuntimeException {
    public InternalServerException(ErrorCode errorCode) {
        super("internal server exception", errorCode);
    }

    public InternalServerException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INTERNAL_SERVER);
    }

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InternalServerException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public InternalServerException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public InternalServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
