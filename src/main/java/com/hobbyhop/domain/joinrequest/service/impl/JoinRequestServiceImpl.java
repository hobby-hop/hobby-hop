package com.hobbyhop.domain.joinrequest.service.impl;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.joinrequest.dto.JoinPageRequestDTO;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.JoinRequestRepository;
import com.hobbyhop.domain.joinrequest.service.JoinRequestService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberAlreadyJoined;
import com.hobbyhop.global.exception.clubmember.ClubMemberRoleException;
import com.hobbyhop.global.exception.joinrequest.JoiningClubCountExceed;
import com.hobbyhop.global.exception.joinrequest.NoSuchRequestException;
import com.hobbyhop.global.exception.joinrequest.PendingRequestException;
import com.hobbyhop.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinRequestServiceImpl implements JoinRequestService {
    private final JoinRequestRepository joinRequestRepository;
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;

    @Override
    @Transactional
    public JoinResponseDTO sendRequest(Long clubId, User user) {
        Club club = clubService.findClub(clubId);

        checkIfAlreadyJoined(clubId, user);
        checkIfPendingRequestExists(clubId, user);
        checkIfMemberLimitReached(user);

        JoinRequest joinRequest = JoinRequest.builder()
                .club(club)
                .user(user)
                .status(JoinRequestStatus.PENDING)
                .build();

        return JoinResponseDTO.fromEntity(joinRequestRepository.save(joinRequest));
    }

    @Override
    @Transactional
    public void processRequest(Long clubId, Long requestId, JoinRequestStatus status, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());
        checkIfAdminRole(clubMember);

        JoinRequest joinRequest = joinRequestRepository.findById(requestId).orElseThrow(NoSuchRequestException::new);
        joinRequest.changeStatus(status);

        if (status.equals(JoinRequestStatus.APPROVED)) {
            clubMemberService.joinClub(joinRequest.getClub(), joinRequest.getUser(), MemberRole.MEMBER);
        }
    }

    @Override
    public PageResponseDTO<JoinResponseDTO> getAllRequests(Long clubId, JoinPageRequestDTO pageRequestDTO, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());
        checkIfAdminRole(clubMember);

        Page<JoinResponseDTO> result = joinRequestRepository.findAllByClubId(clubId, pageRequestDTO);

        return PageResponseDTO.<JoinResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total(result.getTotalElements())
                .build();
    }

    private void checkIfAlreadyJoined(Long clubId, User user) {
        if (clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberAlreadyJoined();
        }
    }

    private void checkIfPendingRequestExists(Long clubId, User user) {
        if (joinRequestRepository.existRequest(clubId, user.getId())) {
            throw new PendingRequestException();
        }
    }

    private void checkIfMemberLimitReached(User user) {
        if (clubMemberService.isMemberLimitReached(user.getId())) {
            throw new JoiningClubCountExceed();
        }
    }

    private void checkIfAdminRole(ClubMember clubMember) {
        if (!clubMember.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new ClubMemberRoleException();
        }
    }
}
