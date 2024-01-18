package com.hobbyhop.domain.joinrequest.service.impl;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.club.entity.Club;

import com.hobbyhop.domain.club.service.impl.ClubServiceImpl;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.JoinRequestRepository;
import com.hobbyhop.domain.user.entity.User;

import com.hobbyhop.test.ClubTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
    private ClubMemberPK clubMemberPk;


    @BeforeEach
    void setUp() {
        joinRequest = JoinRequest.builder()
                .club(TEST_CLUB)
                .user(TEST_USER)
                .build();
        clubMemberPk = ClubMemberPK.builder()
                .club(TEST_CLUB)
                .user(TEST_USER)
                .build();

        clubMember = ClubMember.builder()
                .memberRole(MemberRole.ADMIN)
                .clubMemberPK(clubMemberPk)
                .build();
        joinResponseDTO = JoinResponseDTO.fromEntity(joinRequest);
    }


    @DisplayName("[Send]")
    @Test
    void joinRequest_요청_보내기_성공() {
        given(clubService.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(joinRequestRepository.save(any())).willReturn(joinRequest);

        assertThat(sut.sendRequest(TEST_CLUB_ID, TEST_USER)).isEqualTo(joinResponseDTO);
    }
//    @DisplayName("[Get]")
//    @Test
//    void joinRequest_조회() {
//        // Given
//        given(clubService.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
//
//        sut.getRequestByClub(TEST_CLUB_ID, TEST_USER);
//    }

//    @DisplayName("[Process]")
//    @Test
//    void joinRequest() {
//        given(joinRequestRepository.findById(1L)).willReturn(Optional.ofNullable(joinRequest));
//
//        verify(joinRequestRepository).save(joinRequest);
//        verify(clubMemberService).joinClub(TEST_CLUB, TEST_USER);
//        sut.processRequest(1L, JoinRequestStatus.PENDING);
//
//    }
}