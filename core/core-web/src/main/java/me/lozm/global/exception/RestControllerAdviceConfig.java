package me.lozm.global.exception;

import lombok.extern.slf4j.Slf4j;
import me.lozm.global.model.CommonResponseDto;
import me.lozm.utils.exception.BadRequestException;
import me.lozm.utils.exception.CustomExceptionType;
import me.lozm.utils.exception.InternalServerException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@RestControllerAdvice
public class RestControllerAdviceConfig {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected HttpEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info(">> handleMethodArgumentNotValidException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(CustomExceptionType.INVALID_PARAM, e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    protected HttpEntity<Object> handleBindException(BindException e) {
        log.info(">> handleBindException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(CustomExceptionType.INVALID_PARAM, e.getBindingResult());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected HttpEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info(">> handleHttpRequestMethodNotSupportedException, message: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected HttpEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        log.info(">> handleAccessDeniedException, message: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected HttpEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info(">> handleMissingServletRequestParameterException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected HttpEntity<Object> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.info(">> IllegalArgumentException, message: {}", e.getMessage());
        return CommonResponseDto.badRequest(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    protected HttpEntity<Object> handleBadRequestException(final BadRequestException e) {
        log.info(">> handleBadRequestException, message: {}", isEmpty(e.getMessage()) ? e.getType().getMessage() : e.getMessage());
        return CommonResponseDto.badRequest(e.getType(), e.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    protected HttpEntity<Object> handleInternalServerException(InternalServerException e) {
        log.error(">> InternalServerException, message: {}", e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDto.builder()
                        .code(e.getType().getCode())
                        .message(e.getType().getCode())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    protected HttpEntity<Object> handleException(Exception e) {
        log.error(">> Exception", e);
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDto.builder()
                        .code(CustomExceptionType.SERVICE_ERROR.getCode())
                        .message(CustomExceptionType.SERVICE_ERROR.getMessage())
                        .build());
    }

}