package com.hobbyhop.domain.user.repository;

import com.hobbyhop.domain.user.repository.custom.UserRepositoryCustom;
import java.util.Optional;
import com.hobbyhop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);
}
