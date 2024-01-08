package com.hobbyhop.domain.club.service.impl;

import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.ClubRepository;
import com.hobbyhop.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Override
    public List<ClubResponseDTO> getAllClubs() {
        List<ClubResponseDTO> list = clubRepository.findAll().stream().map(club ->
                ClubResponseDTO.builder()
                        .id(club.getId())
                        .title(club.getTitle())
                        .content(club.getContent())
                        .createdAt(club.getCreatedAt())
                        .modifiedAt(club.getModifiedAt())
                        .build()
        ).collect(Collectors.toList());

        return list;
    }

    @Override
    public ClubResponseDTO getClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow();

        return ClubResponseDTO.fromEntity(club);
    }

    @Override
    @Transactional
    public ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO) {
        Club club = Club.builder()
                .title(clubRequestDTO.getTitle())
                .content(clubRequestDTO.getContent())
                .build();

        Club savedClub = clubRepository.save(club);

        return ClubResponseDTO.fromEntity(savedClub);
    }

    @Override
    @Transactional
    public void removeClubById(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Override
    @Transactional
    public ClubResponseDTO modifyClub(Long clubId, ClubRequestDTO clubRequestDTO) {
        Club club = clubRepository.findById(clubId).orElseThrow();

        if(clubRequestDTO.getTitle() != null) {
            club.changeTitle(clubRequestDTO.getTitle());
        }

        if(clubRequestDTO.getContent() != null) {
            club.changeContent(clubRequestDTO.getContent());
        }

        Club modifiedClub = clubRepository.save(club);

        return ClubResponseDTO.fromEntity(modifiedClub);
    }
}
