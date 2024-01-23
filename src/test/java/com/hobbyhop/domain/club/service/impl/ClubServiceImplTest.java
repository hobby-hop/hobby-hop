package com.hobbyhop.domain.club.service.impl;

import com.hobbyhop.domain.category.service.impl.CategoryServiceImpl;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.repository.ClubRepository;
import com.hobbyhop.domain.club.service.impl.impl.ClubServiceImpl;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.test.ClubTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;


@DisplayName("[Club]")
@ExtendWith(MockitoExtension.class)
@Log4j2
class ClubServiceImplTest implements ClubTest {

    @InjectMocks
    private ClubServiceImpl sut;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private CategoryServiceImpl categoryService;
    @Mock
    private ClubMemberServiceImpl clubMemberService;
    private ClubRequestDTO clubRequestDTO;
    private ClubMember clubMember;
    private ClubMemberPK clubMemberPK;
    private ClubResponseDTO clubResponseDTO;
    private ClubRequestDTO otherClubRequestDTO;

    @BeforeEach
    void setUp() {
        clubMemberPK = ClubMemberPK.builder()
                .club(TEST_CLUB)
                .user(TEST_USER)
                .build();

        clubMember = ClubMember.builder()
                .memberRole(MemberRole.ADMIN)
                .clubMemberPK(clubMemberPK)
                .build();

        clubRequestDTO = ClubRequestDTO.builder()
                .title(TEST_CLUB_TITLE)
                .content(TEST_CLUB_CONTENT)
                .categoryId(TEST_CATEGORY_ID)
                .build();

        clubResponseDTO = ClubResponseDTO.fromEntity(TEST_CLUB);

        otherClubRequestDTO = clubRequestDTO.builder()
                .title(TEST_OTHER_CLUB_TITLE)
                .content(TEST_OTHER_CLUB_CONTENT)
                .categoryId(TEST_OTHER_CATEGORY_ID)
                .build();

    }

    @DisplayName("[GetClub]")
    @Test
    void club_단일_조회() {
        given(clubRepository.findById(TEST_CLUB_ID)).willReturn(Optional.of(TEST_CLUB));

        assertThat(sut.findClub(TEST_CLUB_ID).getId()).isEqualTo(clubResponseDTO.getId());
        assertThat(sut.findClub(TEST_CLUB_ID).getTitle()).isEqualTo(clubResponseDTO.getTitle());
        assertThat(sut.findClub(TEST_CLUB_ID).getContent()).isEqualTo(clubResponseDTO.getContent());
    }

    @DisplayName("[Make]")
    @Test
    void club_생성() {
        // Given
        given(categoryService.findCategory(TEST_CATEGORY_ID)).willReturn(TEST_CATEGORY);
        given(clubRepository.save(any())).willReturn(TEST_CLUB);
        // When & Then
//        verify(clubMemberService.);
        assertThat(sut.makeClub(clubRequestDTO, TEST_USER).getTitle()).isEqualTo(TEST_CLUB_TITLE);
        assertThat(sut.makeClub(clubRequestDTO, TEST_USER).getContent()).isEqualTo(TEST_CLUB_CONTENT);
    }


    @DisplayName("[Modify]")
    @Test
    void club_수정() {
        // Given
        given(clubRepository.findById(TEST_CLUB_ID)).willReturn(Optional.of(TEST_CLUB));
        given(clubMemberService.findByClubAndUser(TEST_CLUB.getId(), TEST_USER.getId())).willReturn(clubMember);
        given(categoryService.findCategory(TEST_OTHER_CATEGORY_ID)).willReturn(TEST_OTHER_CATEGORY);
        given(clubRepository.save(TEST_CLUB)).willReturn(TEST_OTHER_CLUB);

        // When
        ClubResponseDTO clubResponseDTO = sut.modifyClub(TEST_CLUB_ID, otherClubRequestDTO, TEST_USER);

        // Then
        assertThat(clubResponseDTO.getTitle()).isEqualTo(TEST_OTHER_CLUB_TITLE);
        assertThat(clubResponseDTO.getContent()).isEqualTo(TEST_OTHER_CLUB_CONTENT);
        assertThat(clubResponseDTO.getCategoryId()).isEqualTo(TEST_OTHER_CATEGORY_ID);
    }

    @DisplayName("[Remove]")
    @Test
    void club_삭제() {
        // Given
        given(clubRepository.findById(TEST_CLUB_ID)).willReturn(Optional.of(TEST_CLUB));
        given(clubMemberService.findByClubAndUser(TEST_CLUB.getId(), TEST_USER.getId())).willReturn(clubMember);
        willDoNothing().given(clubMemberService).removeMember(TEST_CLUB, TEST_USER);
        willDoNothing().given(clubRepository).delete(TEST_CLUB);

        // When & Then
        assertThatCode(() -> sut.removeClubById(TEST_CLUB_ID, TEST_USER)).doesNotThrowAnyException();
    }
    @DisplayName("[GetMyClubs]")
    @Test
    void club_내가_속한_클럽_리스트_조회() {

    }
}
