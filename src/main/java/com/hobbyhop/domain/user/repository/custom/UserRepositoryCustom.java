package com.hobbyhop.domain.user.repository.custom;

public interface UserRepositoryCustom {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
