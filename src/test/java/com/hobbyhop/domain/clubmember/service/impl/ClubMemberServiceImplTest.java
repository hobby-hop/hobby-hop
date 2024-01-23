package com.hobbyhop.domain.clubmember.service.impl;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.repository.ClubMemberRepository;
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
    }

    @DisplayName("[Join]")
    @Test
    void clubMember_가입_성공() {
        // Given
        given(clubMemberRepository.save(any())).willReturn(clubMember);
        // When
        sut.joinClub(TEST_CLUB, TEST_USER, MemberRole.MEMBER);

        // Then

//        assertThat(clubMemberResponseDTO.getClubId()).isEqualTo(TEST_CLUB_ID);
    }
    @DisplayName("[Remove]")
    @Test
    void clubMember_삭제_성공() {
        // Given
        willDoNothing().given(clubMemberRepository).delete(clubMember);
        given(clubMemberRepository.findByClubMemberPK_Club_IdAndClubMemberPK_User_Id(TEST_CLUB_ID, TEST_USER_ID)).willReturn(Optional.of(clubMember));
        // When
        sut.removeMember(TEST_CLUB, TEST_USER);
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