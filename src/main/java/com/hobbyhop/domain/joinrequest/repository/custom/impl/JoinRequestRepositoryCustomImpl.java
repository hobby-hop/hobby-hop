package com.hobbyhop.domain.joinrequest.repository.custom.impl;

import com.hobbyhop.domain.joinrequest.entity.QJoinRequest;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.joinrequest.repository.custom.JoinRequestRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.hobbyhop.domain.joinrequest.entity.QJoinRequest.joinRequest;

@RequiredArgsConstructor
public class JoinRequestRepositoryCustomImpl implements JoinRequestRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean existRequest(Long clubId, Long userId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(joinRequest)
                .where(joinRequest.club.id.eq(clubId).and(joinRequest.user.id.eq(userId)).and(joinRequest.status.eq(JoinRequestStatus.PENDING)))
                .fetchFirst(); // limit 1

        return fetchOne != null; // 1개가 있는지 없는지 판단 (없으면 null이라 null체크)
    }
}
