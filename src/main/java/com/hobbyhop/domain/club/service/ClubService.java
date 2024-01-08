package com.hobbyhop.domain.club.service;

import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;

import java.util.List;

public interface ClubService {

    List<ClubResponseDTO> getAllClubs();

    ClubResponseDTO getClub(Long clubId);

    ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO);
    void removeClubById(Long clubId);

    ClubResponseDTO modifyClub(Long clubId, ClubRequestDTO clubRequestDTO);

}
