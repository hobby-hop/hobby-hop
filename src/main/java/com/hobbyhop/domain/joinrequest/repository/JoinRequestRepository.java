package com.hobbyhop.domain.joinrequest.repository;

import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.custom.JoinRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long>, JoinRequestRepositoryCustom {

    List<JoinRequest> findByClub_IdAndStatus(Long clubId, JoinRequestStatus status);

}
