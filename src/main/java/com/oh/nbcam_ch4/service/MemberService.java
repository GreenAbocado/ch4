package com.oh.nbcam_ch4.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.oh.nbcam_ch4.common.constant.S3KeyPrefix;
import com.oh.nbcam_ch4.common.exception.CustomException;
import com.oh.nbcam_ch4.common.exception.ErrorCode;
import com.oh.nbcam_ch4.dto.MemberProfileResponse;
import com.oh.nbcam_ch4.dto.MemberRequest;
import com.oh.nbcam_ch4.dto.MemberResponse;
import com.oh.nbcam_ch4.entity.Member;
import com.oh.nbcam_ch4.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;
	private final FileService fileService;

	@Transactional
	public MemberResponse saveMember(MemberRequest request) {
		Member member = memberRepository.save(request.toEntity());
		log.info("회원 저장 | id = {}", member.getId());
		return MemberResponse.from(member);
	}

	// TODO : S3가 트랜잭션 롤백 대상이 아니므로
	//  1. 롤백시 새 파일 삭제 필요 (미 삭제 시, 고아 객체 발생)
	//  2. 커밋 완료 후 기존 파일 삭제 (파일 삭제 후 롤백될 경우, DB는 존재하지 않는 기존 파일 키 보유)
	//  -> 위 로직을 트랜잭션 밖에서 조율 필요
	@Transactional
	public MemberProfileResponse updateProfileImg(MultipartFile file, Long memberId) {
		String key = fileService.upload(file, S3KeyPrefix.PROFILE_IMG);

		try {
			Member member = findMemberById(memberId);
			String oldKey = member.getProfileImgKey();
			member.updateProfileImgKey(key);
			if (oldKey != null) {
				fileService.delete(oldKey); // 이후 프록시 객체에서 롤백할 경우 DB가 존재하지 않는 파일 키를 보유하게 됨
			}

		} catch (Exception e) {	// 프록시 객체에서 수행되는 커밋 단계 예외는 잡지 못함
			fileService.delete(key);
			throw e;
		}
		return new MemberProfileResponse(fileService.getDownloadURL(key));
	}

	@Transactional(readOnly = true)
	public MemberResponse getMember(Long memberId) {
		return MemberResponse.from(findMemberById(memberId));
	}

	@Transactional(readOnly = true)
	public MemberProfileResponse getProfileImg(Long memberId) {
		String key = findMemberById(memberId).getProfileImgKey();
		if (key == null) {
			throw new CustomException(ErrorCode.MEMBER_PROFILE_NOT_FOUND);
		}
		return new MemberProfileResponse(fileService.getDownloadURL(key));
	}

	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}
}