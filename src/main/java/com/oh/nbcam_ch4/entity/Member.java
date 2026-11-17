package com.oh.nbcam_ch4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(nullable = false)
	private int age;

	@Enumerated(EnumType.STRING)
	@Column(length = 4)
	private Mbti mbti;

	private String profileImgKey;

	public static Member of(String name, int age, Mbti mbti) {
		return new Member(name, age, mbti);
	}

	private Member(String name, int age, Mbti mbti) {
		this.name = name;
		this.age = age;
		this.mbti = mbti;
	}

	public void updateProfileImgKey(String key) {
		this.profileImgKey = key;
	}
}
