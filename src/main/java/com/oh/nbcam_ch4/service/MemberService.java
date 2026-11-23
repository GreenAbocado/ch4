package com.oh.nbcam_ch4.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oh.nbcam_ch4.common.exception.CustomException;
import com.oh.nbcam_ch4.common.exception.ErrorCode;
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

	@Transactional
	public MemberResponse saveMember(MemberRequest request) {
		Member member = memberRepository.save(request.toEntity());
		log.info("회원 저장 | id = {}", member.getId());
		return MemberResponse.from(member);
	}

	@Transactional(readOnly = true)
	public MemberResponse getMember(Long memberId) {
		return MemberResponse.from(findMemberById(memberId));
	}

	@Transactional
	public String changeProfileImgKey(Long memberId, String key) {
		Member member = findMemberById(memberId);
		String oldKey = member.getProfileImgKey();
		member.updateProfileImgKey(key);
		return oldKey;
	}

	@Transactional(readOnly = true)
	public String getProfileImgKey(Long memberId) {
		String key = findMemberById(memberId).getProfileImgKey();
		if (key == null) {
			throw new CustomException(ErrorCode.MEMBER_PROFILE_NOT_FOUND);
		}
		return key;
	}

	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}
}