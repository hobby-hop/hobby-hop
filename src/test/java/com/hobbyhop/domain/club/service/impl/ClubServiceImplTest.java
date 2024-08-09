package com.hobbyhop.domain.club.service.impl;

import com.hobbyhop.domain.category.service.impl.CategoryServiceImpl;
import com.hobbyhop.domain.club.dto.ClubModifyDTO;
import com.hobbyhop.domain.club.dto.ClubPageRequestDTO;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.repository.ClubRepository;
import com.hobbyhop.domain.clubmember.dto.ClubMemberResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.global.exception.club.AlreadyExistClubTitle;
import com.hobbyhop.global.exception.joinrequest.JoiningClubCountExceed;
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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@DisplayName("[Club]")
@ExtendWith(MockitoExtension.class)
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
    private ClubMember normalClubMember;
    private ClubMemberPK clubMemberPK;
    private ClubResponseDTO clubResponseDTO;
    private ClubRequestDTO otherClubRequestDTO;
    private ClubMemberResponseDTO clubMemberResponseDTO;
    private ClubModifyDTO clubModifyDTO;
    private ClubPageRequestDTO pageRequestDTO;

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

        normalClubMember = ClubMember.builder()
                .memberRole(MemberRole.MEMBER)
                .clubMemberPK(clubMemberPK)
                .build();

        clubMemberResponseDTO = ClubMemberResponseDTO.builder()
                .clubId(TEST_CLUB_ID)
                .userId(TEST_USER_ID)
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

        clubModifyDTO = ClubModifyDTO.builder()
                .title(TEST_OTHER_CLUB_TITLE)
                .content(TEST_OTHER_CLUB_CONTENT)
                .categoryId(TEST_OTHER_CATEGORY_ID)
                .build();

        int page = 1;
        int size = 10;
        pageRequestDTO = ClubPageRequestDTO.builder()
                .page(1)
                .size(9)
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
        given(clubRepository.existsClubByTitle(TEST_CLUB_TITLE)).willReturn(false);
        given(categoryService.findCategory(clubRequestDTO.getCategoryId())).willReturn(TEST_CATEGORY);

        // When & Then
        assertThat(sut.makeClub(clubRequestDTO, TEST_USER).getTitle()).isEqualTo(TEST_CLUB_TITLE);
        assertThat(sut.makeClub(clubRequestDTO, TEST_USER).getContent()).isEqualTo(TEST_CLUB_CONTENT);
        assertThat(sut.makeClub(clubRequestDTO, TEST_USER).getCategoryId()).isEqualTo(TEST_CATEGORY_ID);
    }

    @DisplayName("[Make] [Fail]")
    @Test
    void club_생성_중복된이름으로인한_실패() {
        // Given
        given(clubRepository.existsClubByTitle(TEST_CLUB_TITLE)).willReturn(true);

        // When & Then
        assertThatCode(() -> sut.makeClub(clubRequestDTO, TEST_USER)).isInstanceOf(AlreadyExistClubTitle.class);
    }

    @DisplayName("[Make] [Fail]")
    @Test
    void club_생성_가입한_모임갯수_초과로인한_실패() {
        // Given
        given(clubMemberService.isMemberLimitReached(TEST_USER_ID)).willReturn(true);

        // When & Then
        assertThatCode(() -> sut.makeClub(clubRequestDTO, TEST_USER)).isInstanceOf(JoiningClubCountExceed.class);
    }

    @DisplayName("[Modify]")
    @Test
    void club_수정() {
        // Given
        given(clubRepository.findById(TEST_CLUB_ID)).willReturn(Optional.of(TEST_CLUB));
        given(clubMemberService.findByClubAndUser(TEST_CLUB.getId(), TEST_USER.getId())).willReturn(clubMember);
        given(categoryService.findCategory(TEST_OTHER_CATEGORY_ID)).willReturn(TEST_OTHER_CATEGORY);

        // When
        ClubResponseDTO clubResponseDTO = sut.modifyClub(TEST_CLUB_ID, clubModifyDTO, TEST_USER);

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

        // When
        sut.removeClubById(TEST_CLUB_ID, TEST_USER);

        // Then
        verify(clubRepository, atLeastOnce()).deleteAllElement(TEST_CLUB_ID);
    }

    @DisplayName("[GetMyClubs]")
    @Test
    void club_내가_속한_클럽_리스트_조회() {
        // Given
        List<ClubMember> list = List.of(clubMember);
        List<ClubResponseDTO> result = List.of(ClubResponseDTO.fromEntity(clubMember.getClubMemberPK().getClub()));
        given(clubMemberService.findByUserId(TEST_USER)).willReturn(list);

        // When & Then
        assertThat(sut.getMyClubs(TEST_USER)).isEqualTo(result);
    }

    @DisplayName("[GetAllClubs]")
    @Test
    void club_모든_모임_리스트_조회() {
        // Given
        long totalCount = 1L;
        List<ClubResponseDTO> list = List.of(clubResponseDTO);
        given(clubRepository.findAll(pageRequestDTO)).willReturn(new PageImpl<>(list, pageRequestDTO.getPageable("id"), totalCount));

        // When
        sut.getAllClubs(pageRequestDTO);

        // Then
        assertThat(sut.getAllClubs(pageRequestDTO).getPage()).isEqualTo(pageRequestDTO.getPage());
        assertThat(sut.getAllClubs(pageRequestDTO).getSize()).isEqualTo(pageRequestDTO.getSize());
        assertThat(sut.getAllClubs(pageRequestDTO).getTotal()).isEqualTo(list.size());
        assertThat(sut.getAllClubs(pageRequestDTO).getDtoList()).isEqualTo(list);
    }
}
