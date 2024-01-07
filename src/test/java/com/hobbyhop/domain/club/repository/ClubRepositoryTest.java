package com.hobbyhop.domain.club.repository;

import com.hobbyhop.domain.club.entity.Club;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubRepositoryTest {
    @Autowired
    ClubRepository clubRepository;

    @Test
    void name() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Club club = Club.builder()
                    .title("test title" + i)
                    .content("test content"+i)
                    .build();
            clubRepository.save(club);
        });
    }
}