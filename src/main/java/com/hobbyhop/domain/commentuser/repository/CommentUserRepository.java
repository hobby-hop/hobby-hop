package com.hobbyhop.domain.commentuser.repository;

import com.hobbyhop.domain.commentuser.entity.CommentUser;
import java.util.Optional;

import com.hobbyhop.domain.commentuser.repository.custom.CommentUserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentUserRepository extends JpaRepository<CommentUser, Long>, CommentUserRepositoryCustom {

}