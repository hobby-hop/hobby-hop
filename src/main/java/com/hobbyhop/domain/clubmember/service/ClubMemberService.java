package com.hobbyhop.domain.clubmember.service;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.user.entity.User;
import java.util.List;


public interface ClubMemberService {

    // 모임에 가입한다.
    void joinClub(Club club, User user);

    //모임을 탈퇴한다.
    void removeMember(Club club, User user);

    ClubMember findByClubAndUser(Club club, User user);

    List<ClubMember> findByUserId(User user);
}
