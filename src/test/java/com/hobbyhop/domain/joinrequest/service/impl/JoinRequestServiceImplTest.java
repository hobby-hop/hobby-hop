package com.hobbyhop.domain.joinrequest.service.impl;

import com.hobbyhop.domain.club.service.impl.ClubServiceImpl;
import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.domain.joinrequest.dto.JoinPageRequestDTO;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.JoinRequestRepository;

import com.hobbyhop.global.exception.clubmember.ClubMemberRoleException;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import com.hobbyhop.test.ClubTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[JoinRequest]")
class JoinRequestServiceImplTest implements ClubTest {
    @InjectMocks
    private JoinRequestServiceImpl sut;
    @Mock
    private ClubServiceImpl clubService;
    @Mock
    private JoinRequestRepository joinRequestRepository;
    @Mock
    private ClubMemberServiceImpl clubMemberService;
    private JoinRequest joinRequest;
    private JoinResponseDTO joinResponseDTO;
    private ClubMember clubMember;
    private ClubMember normalClubMember;
    private ClubMemberPK clubMemberPk;
    private JoinPageRequestDTO pageRequestDTO;

    @BeforeEach
    void setUp() {
        joinRequest = JoinRequest.builder()
                .club(TEST_CLUB)
                .user(TEST_USER)
                .status(JoinRequestStatus.PENDING)
                .build();

        clubMemberPk = ClubMemberPK.builder()
                .club(TEST_CLUB)
                .user(TEST_USER)
                .build();

        clubMember = ClubMember.builder()
                .memberRole(MemberRole.ADMIN)
                .clubMemberPK(clubMemberPk)
                .build();

        normalClubMember = ClubMember.builder()
                .memberRole(MemberRole.MEMBER)
                .clubMemberPK(clubMemberPk)
                .build();

        joinResponseDTO = JoinResponseDTO.fromEntity(joinRequest);
        pageRequestDTO = JoinPageRequestDTO.builder().build();
    }


    @DisplayName("[Send]")
    @Test
    void joinRequest_요청_보내기_성공() {
        // Given
        given(clubService.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(false);
        given(joinRequestRepository.save(any())).willReturn(joinRequest);

        // When&Then
        assertThat(sut.sendRequest(TEST_CLUB_ID, TEST_USER)).isEqualTo(joinResponseDTO);
    }
    @DisplayName("[Get]")
    @Test
    void joinRequest_조회() {
        // Given
        given(clubMemberService.findByClubAndUser(TEST_CLUB_ID, TEST_USER_ID)).willReturn(clubMember);
        given(joinRequestRepository.findByClub_IdAndStatus(TEST_CLUB_ID, JoinRequestStatus.PENDING)).willReturn(List.of(joinRequest));

        // When & Then
        assertThat(sut.getRequestByClub(TEST_CLUB_ID, TEST_USER).get(0).getId()).isEqualTo(joinResponseDTO.getId());
        assertThat(sut.getRequestByClub(TEST_CLUB_ID, TEST_USER).get(0).getUsername()).isEqualTo(joinResponseDTO.getUsername());
        assertThat(sut.getRequestByClub(TEST_CLUB_ID, TEST_USER).get(0).getRecvClubId()).isEqualTo(joinResponseDTO.getRecvClubId());
        assertThat(sut.getRequestByClub(TEST_CLUB_ID, TEST_USER).get(0).getSendUserId()).isEqualTo(joinResponseDTO.getSendUserId());
    }


    @DisplayName("[Get] [AllRequests] [Success]")
    @Test
    void JoinRequest_리스트_조회() {
        // Given
        List<JoinResponseDTO> list = List.of(joinResponseDTO);
        long totalCount = list.stream().count();
        given(clubMemberService.findByClubAndUser(TEST_CLUB_ID, TEST_USER_ID)).willReturn(clubMember);
        given(joinRequestRepository.findAllByClubId(TEST_CLUB_ID, pageRequestDTO)).willReturn(new PageImpl<>(list, pageRequestDTO.getPageable(pageRequestDTO.getSortBy()), totalCount));

        // When & Then
        assertThat(sut.getAllRequests(TEST_CLUB_ID, pageRequestDTO, TEST_USER).getDtoList()).isEqualTo(list);
        assertThat(sut.getAllRequests(TEST_CLUB_ID, pageRequestDTO, TEST_USER).getTotal()).isEqualTo(totalCount);
    }
    @DisplayName("[Get] [AllRequests] [Fail]")
    @Test
    void JoinRequest_페이징_조회_권한이없어서_실패() {
        // Given
        given(clubMemberService.findByClubAndUser(TEST_CLUB_ID, TEST_USER_ID)).willReturn(normalClubMember);

        // When & Then
        assertThatCode(() -> sut.getAllRequests(TEST_CLUB_ID, pageRequestDTO, TEST_USER)).isInstanceOf(ClubMemberRoleException.class);
    }


    @DisplayName("[Process] [Success]")
    @Test
    void joinRequest_처리_성공() {
        // Given
        given(clubMemberService.findByClubAndUser(1L, 1L)).willReturn(clubMember);
        given(joinRequestRepository.findById(1L)).willReturn(Optional.of(joinRequest));

        // When
        sut.processRequest(1L, 1L, JoinRequestStatus.APPROVED, TEST_USER);

        // Then
        verify(clubMemberService).joinClub(TEST_CLUB, TEST_USER, MemberRole.MEMBER);
    }
    @DisplayName("[Process] [Fail]")
    @Test
    void joinRequest_처리_권한이없어서실패() {
        // Given
        given(clubMemberService.findByClubAndUser(1L, 1L)).willReturn(clubMember);
        given(joinRequestRepository.findById(1L)).willReturn(Optional.of(joinRequest));

        // When
        sut.processRequest(1L, 1L, JoinRequestStatus.APPROVED, TEST_USER);

        // Then
        verify(clubMemberService).joinClub(TEST_CLUB, TEST_USER, MemberRole.MEMBER);
    }
}