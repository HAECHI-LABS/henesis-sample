package io.haechi.henesis.assignment.web;

import io.haechi.henesis.assignment.domain.exception.AuthenticationException;
import io.haechi.henesis.assignment.domain.exception.BadRequestException;
import io.haechi.henesis.assignment.domain.exception.ErrorCode;
import io.haechi.henesis.assignment.domain.exception.InternalServerException;
import io.haechi.henesis.assignment.web.error.DefaultExceptionHandler;
import io.haechi.henesis.assignment.web.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler extends DefaultExceptionHandler {
    @ExceptionHandler(value = {
            BadRequestException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBadRequestIncludedErrorCodeException(BadRequestException e) {
        GlobalExceptionHandler.log.error("error message", e);
        return createErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = {
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class,
            InvalidDataAccessApiUsageException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBadRequestException(IllegalArgumentException e) {
        GlobalExceptionHandler.log.error("error message", e);
        return createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({
            AuthenticationException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {
        GlobalExceptionHandler.log.error("error message", e);
        return createErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = {InternalServerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerException(InternalServerException e) {
        GlobalExceptionHandler.log.error("error message", e);
        return createErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIllegalStateException(Exception e) {
        GlobalExceptionHandler.log.error("error message", e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER, e.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unknownException(Exception e) {
        GlobalExceptionHandler.log.error("error message", e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER, "internal server error");
    }
}
