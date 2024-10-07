package com.hobbyhop.domain.postuser.repository;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.postuser.pk.PostUserPK;
import com.hobbyhop.domain.postuser.repository.custom.PostUserRepositoryCustom;
import com.hobbyhop.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostUserRepository extends JpaRepository<PostUser, PostUserPK>, PostUserRepositoryCustom {

}
