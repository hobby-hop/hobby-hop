package com.hobbyhop.domain.club.service;

import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.PageResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ClubService {

    PageResponseDTO<ClubResponseDTO> getAllClubs(PageRequestDTO pageRequestDTO);

    ClubResponseDTO getClub(Long clubId);

    ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO);
    void removeClubById(Long clubId);

    ClubResponseDTO modifyClub(Long clubId, ClubRequestDTO clubRequestDTO);
    Club findClub(Long clubId);

}
