package com.hobbyhop.domain.club.repository;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.custom.ClubRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {

    @Query(value = "SELECT COUNT(*) FROM post WHERE club_id = :clubId", nativeQuery = true)
    Long findByClubId(@Param("clubId") Long clubId);

    boolean existsClubByTitle(String title);
}
