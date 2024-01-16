package com.hobbyhop.domain.clubmember.repository;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    void deleteClubMemberByClub_Id(Long clubId);
    List<ClubMember> findByUser_Id(Long userId);
    Optional<ClubMember> findByClub_IdAndUser_Id(Long clubId, Long userId);

}
