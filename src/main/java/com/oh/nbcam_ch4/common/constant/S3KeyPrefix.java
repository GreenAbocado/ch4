package com.oh.nbcam_ch4.common.constant;

import lombok.Getter;

@Getter
public enum S3KeyPrefix {

	PROFILE_IMG("uploads/profile-img/");


	private final String prefix;

	S3KeyPrefix(String prefix) {
		this.prefix = prefix;
	}
}