package com.hobbyhop.domain.joinrequest.repository.custom;


import com.hobbyhop.domain.joinrequest.dto.JoinPageRequestDTO;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import org.springframework.data.domain.Page;

public interface JoinRequestRepositoryCustom {
    Boolean existRequest(Long clubId, Long userId);

    Page<JoinResponseDTO> findAllByClubId(Long clubId, JoinPageRequestDTO pageRequestDTO);
}
