package io.github.edsonisaac.jogomemoriabackend.controllers;

import io.github.edsonisaac.jogomemoriabackend.exceptions.ObjectNotFoundException;
import io.github.edsonisaac.jogomemoriabackend.exceptions.OperationFailureException;
import io.github.edsonisaac.jogomemoriabackend.exceptions.StandardError;
import io.github.edsonisaac.jogomemoriabackend.exceptions.ValidationException;
import io.github.edsonisaac.jogomemoriabackend.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public StandardError authenticationException(AuthenticationException ex, HttpServletRequest request) {
        return buildStandardError(UNAUTHORIZED, MessageUtils.AUTHENTICATION_FAIL, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public StandardError dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildStandardError(BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public StandardError fileNotFoundException(FileNotFoundException ex, HttpServletRequest request) {
        return buildStandardError(NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public StandardError httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildStandardError(BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return ex.getBindingResult().getFieldErrors().stream().map(error ->
                buildStandardError(BAD_REQUEST, StringUtils.capitalize(error.getField()) + " " + error.getDefaultMessage() + "!", request)
        ).toList();
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public StandardError objectNotFound(ObjectNotFoundException ex, HttpServletRequest request) {
        return buildStandardError(NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(OperationFailureException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public StandardError operationFailureException(OperationFailureException ex, HttpServletRequest request) {
        return buildStandardError(INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public StandardError validationException(ValidationException ex, HttpServletRequest request) {
        return buildStandardError(BAD_REQUEST, ex.getMessage(), request);
    }

    private StandardError buildStandardError(HttpStatus status, String message, HttpServletRequest request) {

        return new StandardError(
                System.currentTimeMillis(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }
}