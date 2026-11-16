package com.oh.nbcam_ch4.common.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler({HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class})
	public ResponseEntity<ErrorResponse> handleFormatException(Exception e) {
		ErrorCode errorCode = ErrorCode.REQUEST_FORMAT_INVALID;
		log.warn("Exception: {} | 형식 오류 | ex: {}", errorCode.getCode(), e.getClass().getSimpleName());
		return buildResponseEntity(errorCode);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILED;
		List<CustomFieldError> list = e.getBindingResult().getFieldErrors().stream()
			.map(CustomFieldError::from).toList();

		log.warn("Exception: {} | DTO 검증 오류 | 오류 필드 수: {}", errorCode.getCode(), list.size());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.fromFieldErrors(errorCode, list));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException() {
		ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILED;
		log.warn("Exception: {} | 쿼리/경로변수 검증 오류", errorCode.getCode());
		return buildResponseEntity(errorCode);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.warn("Exception: {} | 비즈니스 예외", errorCode.getCode());
		return buildResponseEntity(errorCode);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException() {
		ErrorCode errorCode = ErrorCode.REQUEST_URL_INVALID;
		log.warn("Exception: {} | URL 경로 오류", errorCode.getCode());
		return buildResponseEntity(errorCode);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException() {
		ErrorCode errorCode = ErrorCode.REQUEST_METHOD_INVALID;
		log.warn("Exception: {} | HTTP 메서드 오류", errorCode.getCode());
		return buildResponseEntity(errorCode);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportException() {
		ErrorCode errorCode = ErrorCode.REQUEST_MEDIA_INVALID;
		log.warn("Exception: {} | Media Type 미지원", errorCode.getCode());
		return buildResponseEntity(errorCode);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		ErrorCode errorCode = ErrorCode.SERVER_ERROR;
		log.error("Exception: {}", errorCode.getCode(), e);
		return buildResponseEntity(errorCode);
	}

	private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.from(errorCode));
	}
}