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

    // 모든 모임의 정보를 본다.
    PageResponseDTO<ClubResponseDTO> getAllClubs(ClubPageRequestDTO pageRequestDTO);

    // 해당 모임에 대한 정보를 본다.
    ClubResponseDTO getClub(Long clubId);

    // 새로운 모임을 만든다.
    ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO, User user);

    // 모임의 관리자가 모임을 삭제한다.
    void removeClubById(Long clubId, User user);

    // 모임의 정보를 변경한다.
    ClubResponseDTO modifyClub(Long clubId, ClubModifyDTO clubModifyDTO, User user);

    // 다른 도메인에서 참조한다.
    Club findClub(Long clubId);

    // 내가 속한 모임 조회.
    List<ClubResponseDTO> getMyClubs(User user);

    void removeMember(Long clubId, User user);

    Long getClubCount(Long clubId);
}
