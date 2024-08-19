package com.hobbyhop.domain.joinrequest.repository.custom.impl;

import com.hobbyhop.domain.joinrequest.dto.JoinPageRequestDTO;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.custom.JoinRequestRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.hobbyhop.domain.joinrequest.entity.QJoinRequest.joinRequest;

public class JoinRequestRepositoryCustomImpl extends QuerydslRepositorySupport implements JoinRequestRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public JoinRequestRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(JoinRequest.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Boolean existRequest(Long clubId, Long userId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(joinRequest)
                .where(joinRequest.club.id.eq(clubId).and(joinRequest.user.id.eq(userId)).and(joinRequest.status.eq(JoinRequestStatus.PENDING)))
                .fetchFirst(); // limit 1

        return fetchOne != null;
    }

    @Override
    public Page<JoinResponseDTO> findAllByClubId(Long clubId, JoinPageRequestDTO pageRequestDTO) {
        JPAQuery<JoinResponseDTO> query = jpaQueryFactory.
                select(
                        Projections.constructor(
                                JoinResponseDTO.class,
                                joinRequest.id,
                                joinRequest.user.id,
                                joinRequest.club.id,
                                joinRequest.user.username
                        )
                ).from(joinRequest)
                .where(joinRequest.club.id.eq(clubId)
                        .and(joinRequest.status.eq(JoinRequestStatus.PENDING))
                );

        Pageable pageable = pageRequestDTO.getPageable();
        List<JoinResponseDTO> content = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = jpaQueryFactory.select(joinRequest.count())
                .from(joinRequest)
                .where(joinRequest.club.id.eq(clubId)
                        .and(joinRequest.status.eq(JoinRequestStatus.PENDING)))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }
}
