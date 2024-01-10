package com.hobbyhop.domain.cocomment;

package com.hobbyhop.comment.repository;

import com.hobbyhop.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserdId(Long userId);
}
