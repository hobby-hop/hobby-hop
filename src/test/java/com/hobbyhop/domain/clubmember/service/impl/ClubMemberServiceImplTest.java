package com.hobbyhop.domain.clubmember.service.impl;

import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.repository.ClubMemberRepository;
import com.hobbyhop.global.exception.clubmember.ClubMemberAlreadyJoined;
import com.hobbyhop.global.exception.clubmember.ClubMemberRoleException;
import com.hobbyhop.test.ClubTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[ClubMember]")
class ClubMemberServiceImplTest implements ClubTest {
    @InjectMocks
    private ClubMemberServiceImpl sut;

    @Mock
    private ClubMemberRepository clubMemberRepository;
    private ClubMember clubMember;
    private ClubMember adminClubMember;
    private ClubMemberPK clubMemberPk;

    @BeforeEach
    void setUp() {
    clubMemberPk = ClubMemberPK.builder()
            .club(TEST_CLUB)
            .user(TEST_USER)
            .build();
    clubMember = ClubMember.builder()
            .clubMemberPK(clubMemberPk)
            .memberRole(MemberRole.MEMBER)
            .build();
    adminClubMember = ClubMember.builder()
            .clubMemberPK(clubMemberPk)
            .memberRole(MemberRole.ADMIN)
            .build();
    }

    @DisplayName("[Join]")
    @Test
    void clubMember_가입_성공() {
        // Given
        given(clubMemberRepository.save(any())).willReturn(clubMember);
        // When
        ClubMemberResponseDTO clubMemberResponseDTO = sut.joinClub(TEST_CLUB, TEST_USER, MemberRole.MEMBER);
        // Then
        assertThat(clubMemberResponseDTO.getClubId()).isEqualTo(TEST_CLUB_ID);
    }
    @DisplayName("[Join] [Fail]")
    @Test
    void clubMember_가입_이미_가입된_모임으로인한_실패() {
        // Given
        given(clubMemberRepository.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);

        // When & Then
        assertThatCode(() -> sut.joinClub(TEST_CLUB, TEST_USER, MemberRole.MEMBER)).isInstanceOf(ClubMemberAlreadyJoined.class);
    }
    @DisplayName("[Remove]")
    @Test
    void clubMember_탈퇴_성공() {
        // Given
        willDoNothing().given(clubMemberRepository).delete(clubMember);
        given(clubMemberRepository.findByClubMemberPK_Club_IdAndClubMemberPK_User_Id(TEST_CLUB_ID, TEST_USER_ID)).willReturn(Optional.of(clubMember));
        // When
        sut.leaveMember(TEST_CLUB_ID, TEST_USER, TEST_USER_ID);
        // Then
        verify(clubMemberRepository, times(1)).delete(clubMember);
    }
    @DisplayName("[FindByUserId]")
    @Test
    void clubMember_유저가_속한_클럽_리스트_조회() {
        // Given
        List<ClubMember> clubMembers = new ArrayList<>(List.of(ClubMember.builder()
                .clubMemberPK(clubMemberPk)
                .memberRole(MemberRole.MEMBER)
                .build()));
        given(clubMemberRepository.findByClubMemberPK_User_Id(TEST_USER_ID)).willReturn(clubMembers);

        // When & Then
        assertThat(sut.findByUserId(TEST_USER).get(0).getClubMemberPK()).isEqualTo(clubMembers.get(0).getClubMemberPK());
        assertThat(sut.findByUserId(TEST_USER).get(0).getMemberRole()).isEqualTo(clubMembers.get(0).getMemberRole());
    }
}