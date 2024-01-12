package com.hobbyhop.domain.commentuser.repository;

import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.custom.CommentUserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentUserRepository extends JpaRepository<CommentUser, Long>, CommentUserRepositoryCustom {

}
