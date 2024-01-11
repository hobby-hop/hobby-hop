package com.hobbyhop.domain.club.repository.search;

import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubSearch {
    Page<ClubResponseDTO> list(Pageable pageable, String keyword);
}
