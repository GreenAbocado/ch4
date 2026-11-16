package com.oh.nbcam_ch4.dto;

import com.oh.nbcam_ch4.entity.Mbti;
import com.oh.nbcam_ch4.entity.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {
	private final long id;
	private final String name;
	private final int age;
	private final Mbti mbti;

	public static MemberResponse from(Member member) {
		return new MemberResponse(member.getId(), member.getName(), member.getAge(), member.getMbti());
	}
}