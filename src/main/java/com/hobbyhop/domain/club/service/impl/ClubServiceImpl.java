package com.hobbyhop.domain.club.service.impl;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.service.CategoryService;
import com.hobbyhop.domain.club.dto.ClubModifyDTO;
import com.hobbyhop.domain.club.dto.ClubPageRequestDTO;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.ClubRepository;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.club.AlreadyExistClubTitle;
import com.hobbyhop.global.exception.club.ClubNotFoundException;
import com.hobbyhop.global.exception.clubmember.ClubMemberRoleException;
import com.hobbyhop.global.exception.joinrequest.JoiningClubCountExceed;
import com.hobbyhop.global.response.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ClubMemberService clubMemberService;
    private final CategoryService categoryService;

    @Override
    public PageResponseDTO<ClubResponseDTO> getAllClubs(ClubPageRequestDTO pageRequestDTO) {
        Page<ClubResponseDTO> result = clubRepository.findAll(pageRequestDTO);

        return PageResponseDTO.<ClubResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total(Long.valueOf(result.getTotalElements()).intValue())
                .build();
    }

    @Override
    public ClubResponseDTO getClub(Long clubId) {
        Club club = findClub(clubId);

        return ClubResponseDTO.fromEntity(club);
    }

    @Override
    @Transactional
    public ClubResponseDTO makeClub(ClubRequestDTO clubRequestDTO, User user) {
        if (clubMemberService.isMemberLimitReached(user.getId())) {
            throw new JoiningClubCountExceed();
        }

        validateClubTitle(clubRequestDTO.getTitle());
        Category category = categoryService.findCategory(clubRequestDTO.getCategoryId());
        Club club = Club.builder().title(clubRequestDTO.getTitle())
                .content(clubRequestDTO.getContent()).category(category).build();
        Club savedClub = clubRepository.save(club);
        clubMemberService.joinClub(club, user, MemberRole.ADMIN);

        return ClubResponseDTO.fromEntity(savedClub);
    }

    @Override
    @Transactional
    public void removeClubById(Long clubId, User user) {
        Club club = findClub(clubId);
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());
        validateClubRolePermission(clubMember);

        clubRepository.deleteAllElement(club.getId());
    }

    @Override
    @Transactional
    public ClubResponseDTO modifyClub(Long clubId, ClubModifyDTO clubModifyDTO, User user) {
        Club club = findClub(clubId);
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());
        validateClubRolePermission(clubMember);

        if (clubModifyDTO.getTitle() != null) {
            validateClubTitle(clubModifyDTO.getTitle());
        }
        applyChanges(clubModifyDTO, club);

        return ClubResponseDTO.fromEntity(club);
    }

    @Override
    public List<ClubResponseDTO> getMyClubs(User user) {
        List<ClubMember> list = clubMemberService.findByUserId(user);

        return list.stream().map(clubMember -> ClubResponseDTO.fromEntity(
                clubMember.getClubMemberPK().getClub())).collect(Collectors.toList());
    }


    @Override
    public Club findClub(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
    }

    private void validateClubTitle(String clubTitle) {
        if (clubRepository.existsClubByTitle(clubTitle)) {
            throw new AlreadyExistClubTitle();
        }
    }

    private void validateClubRolePermission(ClubMember clubMember) {
        if (clubMember.getMemberRole() != MemberRole.ADMIN) {
            throw new ClubMemberRoleException();
        }
    }

    private void applyChanges(ClubModifyDTO clubModifyDTO, Club club) {
        if (clubModifyDTO.getTitle() != null) {
            club.changeTitle(clubModifyDTO.getTitle());
        }

        if (clubModifyDTO.getContent() != null) {
            club.changeContent(clubModifyDTO.getContent());
        }

        if (clubModifyDTO.getCategoryId() != null) {
            Category category = categoryService.findCategory(clubModifyDTO.getCategoryId());
            club.changeCategory(category);
        }
    }
}