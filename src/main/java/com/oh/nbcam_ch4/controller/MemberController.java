package com.oh.nbcam_ch4.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oh.nbcam_ch4.dto.MemberProfileResponse;
import com.oh.nbcam_ch4.dto.MemberRequest;
import com.oh.nbcam_ch4.dto.MemberResponse;
import com.oh.nbcam_ch4.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<MemberResponse> saveMember(@Valid @RequestBody MemberRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(memberService.saveMember(request));
	}

	@PostMapping("/{id}/profile-image")
	public ResponseEntity<MemberProfileResponse> updateProfileImg(@RequestParam MultipartFile file, @
		Min(0) @PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(memberService.updateProfileImg(file, id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MemberResponse> getMember(@Min(0) @PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(memberService.getMember(id));
	}

	@GetMapping("/{id}/profile-image")
	public ResponseEntity<MemberProfileResponse> getProfileImg(@Min(0) @PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(memberService.getProfileImg(id));
	}
}