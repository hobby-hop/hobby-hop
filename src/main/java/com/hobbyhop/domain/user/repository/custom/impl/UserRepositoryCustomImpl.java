package com.hobbyhop.domain.user.repository.custom.impl;

import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.user.repository.custom.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUsername(String username) {

        Integer fetchFirst = queryFactory
                .selectOne()
                .from(user)
                .where(user.username.eq(username))
                .fetchFirst();

        return fetchFirst != null;
    }

    public boolean existsByEmail(String email) {

        Integer fetchFirst = queryFactory
                .selectOne()
                .from(user)
                .where(user.email.eq(email))
                .fetchFirst();

        return fetchFirst != null;
    }
}
