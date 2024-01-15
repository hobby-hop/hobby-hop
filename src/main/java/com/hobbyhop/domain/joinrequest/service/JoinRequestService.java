package com.hobbyhop.domain.joinrequest.service;

import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.user.entity.User;

import java.util.List;

public interface JoinRequestService {

    JoinResponseDTO sendRequest(Long clubId, User user);
    List<JoinResponseDTO> getRequestByClub(Long ClubId, User user);
    void processRequest(Long requestId, JoinRequestStatus status);
}
