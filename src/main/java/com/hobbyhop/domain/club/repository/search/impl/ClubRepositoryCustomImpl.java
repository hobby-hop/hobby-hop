package com.hobbyhop.domain.club.repository.search.impl;

import static com.hobbyhop.domain.club.entity.QClub.club;

import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.repository.search.ClubRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ClubRepositoryCustomImpl implements ClubRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ClubResponseDTO> findAll(Pageable pageable, String keyword) {
        List<ClubResponseDTO> content = jpaQueryFactory
                .select(
                        Projections.constructor(
                                ClubResponseDTO.class,
                                club.id,
                                club.title,
                                club.category.categoryName,
                                club.content,
                                club.createdAt,
                                club.modifiedAt,
                                club.category.id.as("categoryId")))
                .from(club)
                .fetch();
        int count = content.size();

        return new PageImpl<>(content, pageable, count);
    }
}
