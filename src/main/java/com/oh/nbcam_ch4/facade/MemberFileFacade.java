package com.oh.nbcam_ch4.facade;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.oh.nbcam_ch4.common.constant.S3KeyPrefix;
import com.oh.nbcam_ch4.dto.MemberProfileResponse;
import com.oh.nbcam_ch4.service.FileService;
import com.oh.nbcam_ch4.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFileFacade {
	private final MemberService memberService;
	private final FileService fileService;

	public MemberProfileResponse updateProfileImg(MultipartFile file, Long memberId) {
		String newKey = fileService.upload(file, S3KeyPrefix.PROFILE_IMG);
		String oldKey;

		try {
			oldKey = memberService.changeProfileImgKey(memberId, newKey);
		} catch (Exception e) {
			fileService.delete(newKey);	// DB 롤백 -> S3 새로운 이미지 제거
			throw e;
		}

		if (oldKey != null) {
			fileService.delete(oldKey);	// DB 커밋 -> S3 기존 이미지 제거
		}
		return new MemberProfileResponse(newKey);
	}

	public MemberProfileResponse getProfileImg(Long memeberId) {
		String url = fileService.getDownloadURL(memberService.getProfileImgKey(memeberId));
		return new MemberProfileResponse(url);
	}
}