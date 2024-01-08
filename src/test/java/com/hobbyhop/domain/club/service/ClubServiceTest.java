package com.hobbyhop.domain.club.service;

import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.ClubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("비즈니스 로직 - 모임")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ClubServiceTest {

    @Autowired
    private ClubService sut;

    @MockBean
    private ClubRepository clubRepository;

    @DisplayName("모임 - 생성 - 성공")
    @Test
    void givenClubRequestDTO_whenDoSaveAction_thenReturnsSavedResponseDTO() {
        // Given
        String title = "sample title";
        String content = "sample content";

        ClubRequestDTO clubRequestDTO = ClubRequestDTO.builder()
                .title(title)
                .content("sample content")
                .build();
        // When
        when(clubRepository.save(any())).thenReturn(Club.builder().title(title).content(content).build());

        // Then
        assertThat(sut.makeClub(clubRequestDTO).getTitle()).isEqualTo(title);
        assertThat(sut.makeClub(clubRequestDTO).getContent()).isEqualTo(content);
    }
    @DisplayName("모임 - 정보 변경")
    @Test
    void givenClubRequestDTO_whenDoModifyAction_thenReturnsResponseDTO() {
        // Given
        String title = "sample title";
        String content = "sample content";

        String modiTitle = "modified title";
        String modiContent = "modified content";

        ClubRequestDTO clubRequestDTO = ClubRequestDTO.builder()
                .title(title)
                .content(content)
                .build();
        // When
        when(clubRepository.findById(1L)).thenReturn(Optional.of(Club.builder().title(title).content(content).build()));
        when(clubRepository.save(any())).thenReturn(Club.builder().title(modiTitle).content(modiContent).build());

        // Then
        assertThat(sut.modifyClub(1L, clubRequestDTO).getTitle()).isEqualTo(modiTitle);
        assertThat(sut.modifyClub(1L, clubRequestDTO).getContent()).isEqualTo(modiContent);
    }

    @DisplayName("모임 - 삭제")
    @Test
    void givenClubId_whenDoDeleteAction_thenReturnsNothing() {
        Long id = 1L;

        doNothing().when(clubRepository).deleteById(id);

        assertThatCode(() -> sut.removeClubById(id)).doesNotThrowAnyException();
    }
}