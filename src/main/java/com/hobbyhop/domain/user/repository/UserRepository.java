package com.hobbyhop.domain.user.repository;

import java.util.Optional;

import com.hobbyhop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
