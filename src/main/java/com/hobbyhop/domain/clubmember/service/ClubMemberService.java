package com.hobbyhop.domain.clubmember.service;

import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;

import java.util.List;

public interface ClubMemberService {
    // TODO : 유저 초대, 유저 밴, 멤버 조회
    ClubMemberResponseDTO joinClub(Long clubId, Long userId);
    List<ClubMemberResponseDTO> getMembers(Long clubId);

    ClubMember findMember(Long clubId, Long userId);

    void removeMember(Long clubId, Long userId);






}
