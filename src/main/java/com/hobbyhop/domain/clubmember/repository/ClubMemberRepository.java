package com.hobbyhop.domain.clubmember.repository;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    boolean existsByClubMemberPK_Club_IdAndClubMemberPK_User_Id(Long clubId, Long userId);

    void deleteClubMemberByClubMemberPK_Club_Id(Long clubId);

    List<ClubMember> findByClubMemberPK_User_Id(Long userId);

    Optional<ClubMember> findByClubMemberPK_Club_IdAndClubMemberPK_User_Id(Long clubId,
            Long userId);
}