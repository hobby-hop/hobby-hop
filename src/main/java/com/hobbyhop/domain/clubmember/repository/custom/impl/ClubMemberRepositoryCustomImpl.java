package com.hobbyhop.domain.clubmember.repository.custom.impl;

import static com.hobbyhop.domain.clubmember.entity.QClubMember.clubMember;

import com.hobbyhop.domain.clubmember.repository.custom.ClubMemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClubMemberRepositoryCustomImpl implements ClubMemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public boolean isClubMember(Long clubId, Long userId) {
        Integer i = jpaQueryFactory
                .selectOne()
                .from(clubMember)
                .where(clubMember.clubMemberPK.club.id.eq(clubId).and(clubMember.clubMemberPK.user.id.eq(userId)))
                .fetchFirst();
        return i != null;
    }
}
