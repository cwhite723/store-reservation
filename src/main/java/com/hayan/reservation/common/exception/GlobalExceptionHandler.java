package com.hayan.reservation.common.exception;
import com.hayan.reservation.common.response.ApplicationResponse;
import com.hayan.reservation.common.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApplicationResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        String message = e.getMessage().equals(errorCode.getMessage()) ? errorCode.getMessage() : e.getMessage();

        ApplicationResponse response = ApplicationResponse.error(errorCode.getCode(), message);

        logger.error("CustomException: {}, HTTP Status: {}", message, errorCode.getHttpStatus());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationResponse> handleUnexpectedException(RuntimeException e) {

        ApplicationResponse response = ApplicationResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);

        logger.error("Unexpected Exception: {}", e.getMessage(), e);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String requestURI = request.getRequestURI();

        ApplicationResponse response = ApplicationResponse.error(ErrorCode.REQUEST_VALIDATION_FAIL);

        logger.error("MethodArgumentNotValidException: {}, Request URI: {}", errorMessage, requestURI);

        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApplicationResponse> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        String errorMessage = e.getMessage();
        String requestURI = request.getRequestURI();

        ApplicationResponse response = ApplicationResponse.error(ErrorCode.REQUEST_VALIDATION_FAIL);

        logger.error("ConstraintViolationException: {}, Request URI: {}", errorMessage, requestURI);

        return ResponseEntity.status(BAD_REQUEST).body(response);
    }
}
