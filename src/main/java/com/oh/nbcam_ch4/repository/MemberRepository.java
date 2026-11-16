package com.oh.nbcam_ch4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oh.nbcam_ch4.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
