package com.hobbyhop.domain.club.service.impl;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.CategoryRepository;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.ClubRepository;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.global.exception.category.CategoryNotFoundException;
import com.hobbyhop.global.exception.club.ClubNotFoundException;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PageResponseDTO<ClubResponseDTO> getAllClubs(PageRequestDTO pageRequestDTO) {
        Page<ClubResponseDTO> result = clubRepository.list(pageRequestDTO.getPageable("id"), pageRequestDTO.getKeyword());

        return PageResponseDTO.<ClubResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public ClubResponseDTO getClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        return ClubResponseDTO.fromEntity(club);
    }

    @Override
    @Transactional
    public ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO) {
        Category category = categoryRepository.findById(clubRequestDTO.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        Club club = Club.builder()
                .title(clubRequestDTO.getTitle())
                .content(clubRequestDTO.getContent())
                .category(category)
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
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        if(clubRequestDTO.getTitle() != null) {
            club.changeTitle(clubRequestDTO.getTitle());
        }

        if(clubRequestDTO.getContent() != null) {
            club.changeContent(clubRequestDTO.getContent());
        }

        if(clubRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(clubRequestDTO.getCategoryId()).orElseThrow(
                    CategoryNotFoundException::new);
            club.changeCategory(category);
        }

        Club modifiedClub = clubRepository.save(club);

        return ClubResponseDTO.fromEntity(modifiedClub);
    }

    @Override
    public Club findClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        return club;
    }
}