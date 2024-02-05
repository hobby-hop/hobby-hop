package com.hobbyhop.domain.clubmember.repository.custom;

public interface ClubMemberRepositoryCustom {
    boolean isClubMember(Long clubId, Long userId);
    boolean isAdminMember(Long clubId, Long userId);
    boolean isMemberLimitReached(Long userId);
}
