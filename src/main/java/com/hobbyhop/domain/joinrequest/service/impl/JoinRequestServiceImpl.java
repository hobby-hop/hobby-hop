package com.hobbyhop.domain.joinrequest.service.impl;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.JoinRequestRepository;
import com.hobbyhop.domain.joinrequest.service.JoinRequestService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinRequestServiceImpl implements JoinRequestService {
    private final JoinRequestRepository joinRequestRepository;
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    @Override
    @Transactional
    public JoinResponseDTO sendRequest(Long clubId, User user) {
        Club club = clubService.findClub(clubId);

        JoinRequest joinRequest = JoinRequest.builder()
                .club(club)
                .user(user)
                .status(JoinRequestStatus.PENDING)
                .build();

        // 테이블에 저장한다.
        JoinRequest savedJoinRequest = joinRequestRepository.save(joinRequest);

        return JoinResponseDTO.fromEntity(savedJoinRequest);
    }

    @Override
    public List<JoinResponseDTO> getRequestByClub(Long clubId, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());

        if(!clubMember.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new ClubMemberRoleException();
        }

        List<JoinResponseDTO> list = joinRequestRepository.findByClub_IdAndStatus(clubId, JoinRequestStatus.PENDING).stream()
                .map(joinRequest -> JoinResponseDTO.fromEntity(joinRequest)).collect(Collectors.toList());

        return list;
    }

    @Override
    @Transactional
    public void processRequest(Long requestId, JoinRequestStatus status) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId).orElseThrow();
        joinRequest.changeStatus(status);

        joinRequestRepository.save(joinRequest);

        if(status.equals(JoinRequestStatus.APPROVED)) {
            clubMemberService.joinClub(joinRequest.getClub().getId(), joinRequest.getUser());
        }
    }


}
