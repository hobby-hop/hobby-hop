package com.hobbyhop.global.exception.common;

import com.hobbyhop.global.exception.jwt.JwtInvalidException;
import com.hobbyhop.global.exception.jwt.NoJwtException;
import com.hobbyhop.global.exception.jwt.UnsupportedGrantTypeException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoJwtException.class)
    protected ResponseEntity<ErrorResponse> handleNoJwtException(NoJwtException e) {

        log.error("NoJwtException", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnsupportedGrantTypeException.class)
    protected ResponseEntity<ErrorResponse> handleUnSupportedGrantTypeException(
            UnsupportedGrantTypeException e) {

        log.error("UnSupportedGrantTypeException", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(JwtInvalidException.class)
    protected ResponseEntity<ErrorResponse> handleJwtInvalidException(JwtInvalidException e) {

        log.error("JwtInvalidException", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {

        log.error("handleBindException", e);

        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {

        log.error("handleHttpRequestMethodNotSupportedException", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleConflict(BusinessException e) {

        log.error("BusinessException", e);

        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, List.of(e.getMessage()));

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageConversionException(
            HttpMessageConversionException e) {

        log.error("HttpMessageConversionException", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {

        log.error("HttpMediaTypeNotSupportedException", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error("Exception", e);

        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
                List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
