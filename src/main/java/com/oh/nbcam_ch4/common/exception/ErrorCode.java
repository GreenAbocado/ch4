package com.oh.nbcam_ch4.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	REQUEST_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "G001","요청 형식이 잘못되었습니다."),
	REQUEST_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "G002","입력값 검증에 실패하였습니다."),
	REQUEST_URL_INVALID(HttpStatus.NOT_FOUND,"G003","URL 경로가 잘못되었습니다"),
	REQUEST_METHOD_INVALID(HttpStatus.METHOD_NOT_ALLOWED,"G004","HTTP 메서드가 잘못되었습니다."),
	REQUEST_MEDIA_INVALID(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "G005", "지원하지 않는 미디어 타입입니다."),

	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "서버 내부 오류입니다."),
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "G501", "파일 업로드를 실패하였습니다"),

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "팀원을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}