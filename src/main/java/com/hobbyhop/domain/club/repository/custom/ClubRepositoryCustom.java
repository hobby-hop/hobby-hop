package com.hobbyhop.domain.club.repository.custom;

import com.hobbyhop.domain.club.dto.ClubPageRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import org.springframework.data.domain.Page;

public interface ClubRepositoryCustom {
    Page<ClubResponseDTO> findAll(ClubPageRequestDTO pageRequestDTO);

    void deleteAllElement(Long clubId);

}
