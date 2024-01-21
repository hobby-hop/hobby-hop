package com.hobbyhop.domain.comment.repository;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.custom.CommentRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}
