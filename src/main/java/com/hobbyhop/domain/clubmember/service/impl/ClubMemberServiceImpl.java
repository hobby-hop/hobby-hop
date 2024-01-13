package com.hobbyhop.domain.clubmember.service.impl;


import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.repository.ClubMemberRepository;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubMemberServiceImpl implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;
    private final ClubService clubService;
    private final UserService userService;

    @Override
    public ClubMemberResponseDTO joinClub(Long clubId, Long userId) {
        //클럽이 있는지 확인
        Club club = clubService.findClub(clubId);
        //유저가 있는지 확인
        User user = userService.findById(userId);
        //초대
        ClubMember clubMember = ClubMember.builder()
                .club(club)
                .user(user)
                .build();

        ClubMember savedClubMember = clubMemberRepository.save(clubMember);

        return ClubMemberResponseDTO.fromEntity(savedClubMember);
    }

    @Override
    public List<ClubMemberResponseDTO> getMembers(Long clubId) {
        List<ClubMember> list = clubMemberRepository.findAllByClub_Id(clubId);

        List<ClubMemberResponseDTO> responseDTOS = list.stream().map(clubMember ->
            ClubMemberResponseDTO.builder()
                    .id(clubMember.getId())
                    .userId(clubMember.getUser().getId())
                    .build()).collect(Collectors.toList());

        return responseDTOS;
    }

    @Override
    public ClubMember findMember(Long clubId, Long userId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_Id(clubId, userId).orElseThrow(ClubMemberNotFoundException::new);
        return clubMember;
    }

    @Override
    public void removeMember(Long clubId, Long userId) {
        ClubMember clubMember = clubMemberRepository.findByClub_IdAndUser_Id(clubId, userId).orElseThrow(ClubMemberNotFoundException::new);
        clubMemberRepository.delete(clubMember);
    }
}
