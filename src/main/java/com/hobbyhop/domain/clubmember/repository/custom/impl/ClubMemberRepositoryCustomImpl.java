package com.hobbyhop.domain.clubmember.repository.custom.impl;

import static com.hobbyhop.domain.clubmember.entity.QClubMember.clubMember;

import com.hobbyhop.domain.clubmember.enums.MemberRole;
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

    @Override
    public boolean isAdminMember(Long clubId, Long userId) {
        Integer i = jpaQueryFactory
                .selectOne()
                .from(clubMember)
                .where(clubMember.clubMemberPK.club.id.eq(clubId).and(clubMember.clubMemberPK.user.id.eq(userId)).and(clubMember.memberRole.eq(MemberRole.ADMIN)))
                .fetchFirst();

        return i != null;
    }

    @Override
    public boolean isMemberLimitReached(Long userId) {
        int limitCount = 9;
        long i = jpaQueryFactory
                .selectOne()
                .from(clubMember)
                .where(clubMember.clubMemberPK.user.id.eq(userId))
                .fetchCount();

        if (i >= limitCount) {
            return true;
        }
        return false;
    }
}
