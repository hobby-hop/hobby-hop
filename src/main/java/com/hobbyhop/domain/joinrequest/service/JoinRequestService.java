package com.hobbyhop.domain.joinrequest.service;

import com.hobbyhop.domain.joinrequest.dto.JoinPageRequestDTO;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.response.PageResponseDTO;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;

import java.util.List;

public interface JoinRequestService {

    JoinResponseDTO sendRequest(Long clubId, User user);
    void processRequest(Long clubId, Long requestId, JoinRequestStatus status, User user);
    PageResponseDTO<JoinResponseDTO> getAllRequests(Long clubId, JoinPageRequestDTO pageRequestDTO, User user);
}
