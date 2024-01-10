//package com.hobbyhop.domain.club.repository;
//
//import com.hobbyhop.domain.club.entity.Club;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Log4j2
//class ClubRepositoryTest {
//    @Autowired
//    ClubRepository clubRepository;
//    @DisplayName("테스트 db 테스트를 위한 메소드")
//    @Test
//    void givenClubId_whenDoSelectAction_thenReturnsEntity() {
//        // Given
//        Club club = Club.builder()
//                .title("test")
//                .build();
//
//        Club savedClub = clubRepository.save(club);
//
//        Long clubId = savedClub.getId();
//
//        // When
//        Club foundClub = clubRepository.findById(clubId).orElseThrow();
//        // Then
//        log.info(foundClub);
//    }
//}