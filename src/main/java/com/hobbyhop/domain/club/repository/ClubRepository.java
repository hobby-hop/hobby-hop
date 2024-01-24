package com.hobbyhop.domain.club.repository;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.custom.ClubRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {
    Optional<Club> findByTitle(String title);
}
