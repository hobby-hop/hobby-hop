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
    public void joinClub(Club club, User user) {
        // 클럽에 이미 가입되어있는지 확인
        if (clubMemberRepository.existsByClubMemberPK_Club_IdAndClubMemberPK_User_Id(club.getId(),
                user.getId())) {
            throw new ClubMemberAlreadyJoined();
        }

        // 가입시키기
        ClubMember clubMember = ClubMember.builder()
                .clubMemberPK(ClubMemberPK.builder()
                        .club(club)
                        .user(user)
                        .build())
                .memberRole(MemberRole.MEMBER).build();
        ClubMember savedClubMember = clubMemberRepository.save(clubMember);
        ClubMemberResponseDTO.fromEntity(savedClubMember);
    }

    @Override
    @Transactional
    public void removeMember(Club club, User user) {
        ClubMember clubMember = clubMemberRepository.findByClubMemberPK_Club_IdAndClubMemberPK_User_Id(
                club.getId(), user.getId()).orElseThrow(ClubMemberNotFoundException::new);
        clubMemberRepository.delete(clubMember);
    }

    @Override
    public ClubMember findByClubAndUser(Club club, User user) {
        return clubMemberRepository.findByClubMemberPK_Club_IdAndClubMemberPK_User_Id(club.getId(),
                user.getId()).orElseThrow(ClubMemberNotFoundException::new);
    }

    @Override
    public List<ClubMember> findByUserId(User user){
        return clubMemberRepository.findByClubMemberPK_User_Id(user.getId());
    }
}
