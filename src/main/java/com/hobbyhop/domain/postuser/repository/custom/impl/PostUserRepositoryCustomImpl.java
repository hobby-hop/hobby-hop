package com.hobbyhop.domain.postuser.repository.custom.impl;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.postuser.repository.custom.PostUserRepositoryCustom;
import com.hobbyhop.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;

@RequiredArgsConstructor
public class PostUserRepositoryCustomImpl implements PostUserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<PostUser> findPostUser(Long userId, Long postId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(postUser)
                .where(postUser.postUserPK.user.id.eq(userId)
                        .and(postUser.postUserPK.post.id.eq(postId)))
                .fetchOne());
    }
}
