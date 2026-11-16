package com.oh.nbcam_ch4.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
}