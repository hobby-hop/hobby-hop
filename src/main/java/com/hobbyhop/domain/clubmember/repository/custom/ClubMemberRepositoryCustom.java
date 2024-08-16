package com.hobbyhop.domain.clubmember.repository.custom;

import com.hobbyhop.domain.clubmember.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepositoryCustom {
    Optional<ClubMember> findClubMember(Long clubId, Long userId);
    boolean isClubMember(Long clubId, Long userId);
    boolean isAdminMember(Long clubId, Long userId);
    boolean isMemberLimitReached(Long userId);
}
