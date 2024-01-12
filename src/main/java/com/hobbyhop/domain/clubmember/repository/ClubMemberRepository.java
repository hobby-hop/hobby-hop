package com.hobbyhop.domain.clubmember.repository;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
}
