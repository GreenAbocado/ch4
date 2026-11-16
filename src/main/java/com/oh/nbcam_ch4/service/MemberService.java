package com.oh.nbcam_ch4.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oh.nbcam_ch4.dto.MemberRequest;
import com.oh.nbcam_ch4.dto.MemberResponse;
import com.oh.nbcam_ch4.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponse saveMember(MemberRequest request) {
		return MemberResponse.from(
			memberRepository.save(request.toEntity()));
	}

	@Transactional(readOnly = true)
	public MemberResponse getMember(Long id) {
		return MemberResponse.from(
			memberRepository.findById(id).orElseThrow());
	}
}
