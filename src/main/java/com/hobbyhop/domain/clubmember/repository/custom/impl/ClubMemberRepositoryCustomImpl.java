package com.hobbyhop.domain.clubmember.repository.custom.impl;

import static com.hobbyhop.domain.club.entity.QClub.club;
import static com.hobbyhop.domain.clubmember.entity.QClubMember.clubMember;

import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.repository.custom.ClubMemberRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ClubMemberRepositoryCustomImpl implements ClubMemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ClubMember> findClubMember(Long clubId, Long userId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(clubMember)
                .where(clubMember.clubMemberPK.club.id.eq(clubId)
                        .and(clubMember.clubMemberPK.user.id.eq(userId)))
                .fetchOne()
        );
    }

    @Override
    public List<ClubResponseDTO> findClubsByUserId(Long userId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                ClubResponseDTO.class,
                                club.id,
                                club.title,
                                club.content,
                                club.category.id.as("categoryId"),
                                club.category.categoryName,
                                club.createdAt,
                                club.modifiedAt
                        ))
                .from(clubMember)
                .join(clubMember.clubMemberPK.club)
                .where(clubMember.clubMemberPK.user.id.eq(userId))
                .fetch();
    }

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
    public boolean isClubLimitReached(Long userId) {
        int limitCount = 9;
        long currentMyClubCount = jpaQueryFactory
                .select(clubMember.count())
                .from(clubMember)
                .where(clubMember.clubMemberPK.user.id.eq(userId))
                .fetchOne();

        return currentMyClubCount >= limitCount;
    }
}
