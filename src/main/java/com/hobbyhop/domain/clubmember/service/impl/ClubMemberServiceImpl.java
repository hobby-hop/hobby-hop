package com.hobbyhop.domain.clubmember.service.impl;


import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.repository.ClubMemberRepository;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberAlreadyJoined;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberServiceImpl implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;
    private final ClubService clubService;

    @Override
    @Transactional
    public ClubMemberResponseDTO joinClub(Long clubId, User user) {
        //클럽이 있는지 확인
        Club club = clubService.findClub(clubId);

        // 클럽에 이미 가입되어있는지 확인
        clubMemberRepository.findByClub_IdAndUser_Id(clubId, user.getId()).ifPresent(existingClubMember -> {
            throw new ClubMemberAlreadyJoined();
        });

        // 가입시키기
        ClubMember clubMember = ClubMember.builder()
                .club(club)
                .user(user)
                .memberRole(MemberRole.MEMBER)
                .build();

        ClubMember savedClubMember = clubMemberRepository.save(clubMember);

        return ClubMemberResponseDTO.fromEntity(savedClubMember);
    }


    @Override
    @Transactional
    public void removeMember(Long clubId, User user) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_Id(clubId, user.getId()).orElseThrow(ClubMemberNotFoundException::new);
        clubMemberRepository.delete(clubMember);
    }

    @Override
    public ClubMember findByClubAndUser(Long clubId, Long userId) {
        return clubMemberRepository.findByClub_IdAndUser_Id(clubId, userId).orElseThrow(ClubMemberNotFoundException::new);
    }
}
