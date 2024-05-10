package com.hobbyhop.domain.club.service;

import com.hobbyhop.domain.club.dto.ClubModifyDTO;
import com.hobbyhop.domain.club.dto.ClubPageRequestDTO;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.response.PageResponseDTO;

import java.util.List;

public interface ClubService {

    PageResponseDTO<ClubResponseDTO> getAllClubs(ClubPageRequestDTO pageRequestDTO);

    ClubResponseDTO getClub(Long clubId);

    ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO, User user);

    void removeClubById(Long clubId, User user);

    ClubResponseDTO modifyClub(Long clubId, ClubModifyDTO clubModifyDTO, User user);

    Club findClub(Long clubId);

    List<ClubResponseDTO> getMyClubs(User user);

    Long getClubCount(Long clubId);
}
