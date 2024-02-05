package com.hobbyhop.domain.clubmember.service.impl;


import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.repository.ClubMemberRepository;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberAlreadyJoined;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberServiceImpl implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    @Transactional
    public ClubMemberResponseDTO joinClub(Club club, User user, MemberRole memberRole) {
        if (isClubMember(club.getId(), user.getId())) {
            throw new ClubMemberAlreadyJoined();
        }

        ClubMember clubMember = ClubMember.builder()
                .clubMemberPK(ClubMemberPK.builder()
                        .club(club)
                        .user(user)
                        .build())
                .memberRole(memberRole).build();
        ClubMember savedClubMember = clubMemberRepository.save(clubMember);

        return ClubMemberResponseDTO.fromEntity(savedClubMember);
    }

    @Override
    @Transactional
    public void removeMember(Club club, User user) {
        ClubMember clubMember = findByClubAndUser(club.getId(), user.getId());
        clubMemberRepository.delete(clubMember);
    }

    @Override
    public ClubMember findByClubAndUser(Long clubId, Long userId) {
        return clubMemberRepository.findByClubMemberPK_Club_IdAndClubMemberPK_User_Id(clubId, userId)
                .orElseThrow(ClubMemberNotFoundException::new);
    }

    @Override
    public List<ClubMember> findByUserId(User user){
        return clubMemberRepository.findByClubMemberPK_User_Id(user.getId());
    }

    @Override
    public boolean isClubMember(Long clubId, Long userId){
        return clubMemberRepository.isClubMember(clubId, userId);
    }

    @Override
    public boolean isAdminMember(Long clubId, Long userId) {
        return clubMemberRepository.isAdminMember(clubId, userId);
    }

    @Override
    public boolean isMemberLimitReached(Long userId) {
        return clubMemberRepository.isMemberLimitReached(userId);
    }
}
