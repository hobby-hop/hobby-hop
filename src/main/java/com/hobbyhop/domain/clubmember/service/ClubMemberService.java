package com.hobbyhop.domain.clubmember.service;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.user.entity.User;
import java.util.List;


public interface ClubMemberService {

    // 모임에 가입한다.
    void joinClub(Long clubId, User user);

    //모임을 탈퇴한다.
    void removeMember(Long clubId, User user);

    ClubMember findByClubAndUser(Long clubId, Long userId);

    List<ClubMember> findByUserId(Long userId);

    void removeClubMemberByClub_Id(Long clubId);
}
