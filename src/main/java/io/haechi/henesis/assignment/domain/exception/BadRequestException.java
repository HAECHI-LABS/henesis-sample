package io.haechi.henesis.assignment.domain.exception;

public class BadRequestException extends DefaultRuntimeException {
    public BadRequestException(ErrorCode errorCode) {
        super("bad request exception", errorCode);
    }

    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause, ErrorCode.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public BadRequestException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}