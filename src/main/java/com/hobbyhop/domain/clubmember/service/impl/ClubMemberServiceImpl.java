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
import com.hobbyhop.global.exception.clubmember.ClubMemberLeaveFailException;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import java.util.List;

import com.hobbyhop.global.exception.clubmember.ClubMemberRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
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
    public void leaveMember(Long clubId, User user, Long userId) {
        ClubMember requestMember = findByClubAndUser(clubId, user.getId());
        ClubMember targetClubMember = findByClubAndUser(clubId, userId);

        if(user.getId() != userId) {
            if(requestMember.getMemberRole() != MemberRole.ADMIN) {
                throw new ClubMemberRoleException();
            } else {
                clubMemberRepository.delete(targetClubMember);
            }
        } else {
            if(requestMember.getMemberRole() != MemberRole.ADMIN) {
                clubMemberRepository.delete(requestMember);
            } else {
                throw new ClubMemberLeaveFailException();
            }
        }
    }

    @Override
    public ClubMember findByClubAndUser(Long clubId, Long userId) {
        return clubMemberRepository.findClubMember(clubId, userId)
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
