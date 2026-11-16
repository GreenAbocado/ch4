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
	public MemberResponse getMember(Long id) {
		return MemberResponse.from(
			memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
	}
}
