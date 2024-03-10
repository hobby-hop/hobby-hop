package com.hobbyhop.domain.clubmember.service;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.user.entity.User;

import java.lang.reflect.Member;
import java.util.List;


public interface ClubMemberService {

    ClubMemberResponseDTO joinClub(Club club, User user, MemberRole memberRole);

    void removeMember(Club club, User user);

    ClubMember findByClubAndUser(Long clubId, Long userId);

    List<ClubMember> findByUserId(User user);

    boolean isClubMember(Long clubId, Long userId);

    boolean isAdminMember(Long clubId,Long userId);

    boolean isMemberLimitReached(Long userId);
}
