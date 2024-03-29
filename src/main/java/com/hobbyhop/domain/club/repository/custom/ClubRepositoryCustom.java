package com.hobbyhop.domain.club.repository.custom;

import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubRepositoryCustom {
    Page<ClubResponseDTO> findAll(Pageable pageable, String keyword);
    void deleteAllElement(Long clubId);
}
