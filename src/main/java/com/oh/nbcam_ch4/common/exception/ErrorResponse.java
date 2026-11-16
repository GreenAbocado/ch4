package com.oh.nbcam_ch4.common.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final String message;
	private final String code;
	private final LocalDateTime timestamp;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final List<CustomFieldError> fieldErrors;

	private ErrorResponse(String message, String code, List<CustomFieldError> fieldErrors) {
		this.message = message;
		this.code = code;
		this.timestamp = LocalDateTime.now();
		this.fieldErrors = fieldErrors;
	}

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getMessage(), errorCode.getCode(), null);
	}

	public static ErrorResponse fromFieldErrors(ErrorCode errorCode, List<CustomFieldError> fieldErrors) {
		return new ErrorResponse(errorCode.getMessage(), errorCode.getCode(), fieldErrors);
	}
}