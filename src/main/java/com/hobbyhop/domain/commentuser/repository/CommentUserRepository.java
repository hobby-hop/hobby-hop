package com.hobbyhop.domain.commentuser.repository;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.custom.CommentUserRepositoryCustom;
import com.hobbyhop.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentUserRepository extends JpaRepository<CommentUser, Long>, CommentUserRepositoryCustom {
    Optional<CommentUser> findByCommentUserPK_CommentAndCommentUserPK_User(Comment comment, User user);
    int countByCommentUserPK_Comment(Comment comment);
}
