package com.hobbyhop.domain.club.repository;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.search.ClubRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {


}
