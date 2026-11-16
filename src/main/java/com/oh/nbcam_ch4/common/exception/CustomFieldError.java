package com.oh.nbcam_ch4.common.exception;

import org.springframework.validation.FieldError;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomFieldError {
	private final String name;
	private final String message;

	public static CustomFieldError from(FieldError fieldError) {
		return new CustomFieldError(fieldError.getField(), fieldError.getDefaultMessage());
	}
}