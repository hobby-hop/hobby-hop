package com.hobbyhop.domain.club.repository.search.impl;

import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.entity.QClub;
import com.hobbyhop.domain.club.repository.search.ClubSearch;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ClubSearchImpl extends QuerydslRepositorySupport implements ClubSearch {
    public ClubSearchImpl() {
        super(Club.class);
    }

    @Override
    public Page<ClubResponseDTO> list(Pageable pageable, String keyword) {
        QClub club = QClub.club;

        JPQLQuery<Club> query = from(club);
        // 제목 검색
        if (keyword != null) {
            query.where(club.title.contains(keyword));
        }

        this.getQuerydsl().applyPagination(pageable, query);

        //
        JPQLQuery<ClubResponseDTO> dtoQuery = query.select(Projections.bean(ClubResponseDTO.class,
                club.id,
                club.title,
                club.content,
                club.createdAt,
                club.modifiedAt,
                club.category.id.as("categoryId")
        ));
        List<ClubResponseDTO> list = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
