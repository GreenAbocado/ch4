package com.oh.nbcam_ch4.file;

import java.util.Arrays;

import com.oh.nbcam_ch4.common.exception.CustomException;
import com.oh.nbcam_ch4.common.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileType {
	JPEG("image/jpeg", ".jpg"),
	PNG("image/png", ".png"),
	GIF("image/gif", ".gif");

	private final String contentType;
	private final String extension;
	private static final FileType[] fileTypes = FileType.values();

	public static FileType fromContentType(String contentType) {
		return Arrays.stream(fileTypes)
				.filter((fileType) -> fileType.getContentType().equals(contentType))
				.findFirst()
				.orElseThrow(()-> new CustomException(ErrorCode.REQUEST_MIME_INVALID));
	}
}