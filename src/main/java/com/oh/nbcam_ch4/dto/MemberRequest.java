package com.oh.nbcam_ch4.dto;

import com.oh.nbcam_ch4.entity.Mbti;
import com.oh.nbcam_ch4.entity.Member;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberRequest(
	@NotBlank(message = "이름이 입력되지 않습니다.")
	@Size(max = 20, message = "이름은 20자 이내로 입력해야 합니다.")
	String name,

	@NotNull(message = "나이가 입력되지 않았습니다.")
	@Min(value = 0, message = "나이는 0살 이상으로 입력해야 합니다.")
	@Max(value = 200, message = "나이는 200살 이내로 입력해야 합니다.")
	Integer age,

	Mbti mbti)
{
	public MemberRequest {
		name = (name == null) ? null : name.trim();
	}

	public Member toEntity() {
		return Member.of(name, age, mbti);
	}
}