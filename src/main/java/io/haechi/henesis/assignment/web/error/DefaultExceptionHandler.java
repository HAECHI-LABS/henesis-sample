package io.haechi.henesis.assignment.web.error;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import io.haechi.henesis.assignment.domain.exception.AuthenticationException;
import io.haechi.henesis.assignment.domain.exception.BadRequestException;
import io.haechi.henesis.assignment.domain.exception.ErrorCode;
import io.haechi.henesis.assignment.domain.exception.InternalServerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionHandler {
    public DefaultExceptionHandler() {
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBadRequestIncludedErrorCodeException(BadRequestException e) {
        return this.createErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleAuthenticationException(AuthenticationException e) {
        return this.createErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler({InternalServerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleInternalServerException(InternalServerException e) {
        return this.createErrorResponse(e.getErrorCode(), e.getMessage());
    }

    protected ErrorResponse createErrorResponse(ErrorCode errorCode, String responseMessage) {
        return new ErrorResponse(new ErrorBody(responseMessage, errorCode.getValue()));
    }
}
