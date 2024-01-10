//package com.hobbyhop.domain.club.service;
//
//import com.hobbyhop.domain.category.entity.Category;
//import com.hobbyhop.domain.category.enums.HobbyCategory;
//import com.hobbyhop.domain.category.repository.CategoryRepository;
//import com.hobbyhop.domain.club.dto.ClubRequestDTO;
//import com.hobbyhop.domain.club.dto.ClubResponseDTO;
//import com.hobbyhop.domain.club.entity.Club;
//import com.hobbyhop.domain.club.repository.ClubRepository;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@DisplayName("비즈니스 로직 - 모임")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@Log4j2
//class ClubServiceTest {
//
//    @Autowired
//    private ClubService sut;
//
//    @MockBean
//    private ClubRepository clubRepository;
//    @MockBean
//    private CategoryRepository categoryRepository;
//
//    @DisplayName("모임 - 생성 - 성공")
//    @Test
//    void givenClubRequestDTO_whenDoSaveAction_thenReturnsSavedResponseDTO() {
//        // Given
//        String title = "sample title";
//        String content = "sample content";
//        Category category = Category.builder()
//                .hobbyCategory(HobbyCategory.MUSIC)
//                .build();
//
//
//        ClubRequestDTO clubRequestDTO = ClubRequestDTO.builder()
//                .title(title)
//                .content("sample content")
//                .categoryId(1L)
//                .build();
//        // When
//        when(clubRepository.save(any())).thenReturn(Club.builder().title(title).content(content).category(category).build());
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mock(Category.class)));
//        // Then
//        assertThat(sut.makeClub(clubRequestDTO).getTitle()).isEqualTo(title);
//        assertThat(sut.makeClub(clubRequestDTO).getContent()).isEqualTo(content);
//    }
//
//    @DisplayName("모임 - 정보 변경")
//    @Test
//    void givenClubRequestDTO_whenDoModifyAction_thenReturnsResponseDTO() {
//        // Given
//        String title = "sample title";
//        String content = "sample content";
//
//        String modiTitle = "modified title";
//        String modiContent = "modified content";
//        Long modiCategoryId = 2L;
//
//        ClubRequestDTO clubRequestDTO = ClubRequestDTO.builder()
//                .title(title)
//                .content(content)
//                .categoryId(2L)
//                .build();
//
//        Category category = Category.builder()
//                .id(1L)
//                .hobbyCategory(HobbyCategory.SPORTS)
//                .build();
//        Category modiCategory = Category.builder()
//                .id(2L)
//                .hobbyCategory(HobbyCategory.MUSIC)
//                .build();
//
//        when(clubRepository.findById(1L)).thenReturn(Optional.of(Club.builder().title(title).content(content).category(category).build()));
//        when(categoryRepository.findById(modiCategoryId)).thenReturn(Optional.of(Category.builder().hobbyCategory(HobbyCategory.MUSIC).build()));
//        when(clubRepository.save(any())).thenReturn(Club.builder().title(modiTitle).content(modiContent).category(modiCategory).build());
//
//        // when
//        ClubResponseDTO clubResponseDTO = sut.modifyClub(1L, clubRequestDTO);
//        log.info(clubResponseDTO);
//        // Then
//        assertThat(clubResponseDTO.getTitle()).isEqualTo(modiTitle);
//        assertThat(clubResponseDTO.getContent()).isEqualTo(modiContent);
//        assertThat(clubResponseDTO.getCategoryId()).isEqualTo(modiCategoryId);
//    }
//
//    @DisplayName("모임 - 삭제")
//    @Test
//    void givenClubId_whenDoDeleteAction_thenReturnsNothing() {
//        Long id = 1L;
//
//        doNothing().when(clubRepository).deleteById(id);
//
//        assertThatCode(() -> sut.removeClubById(id)).doesNotThrowAnyException();
//    }
//}