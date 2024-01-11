package com.hobbyhop.global.security.userdetails;

import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String email) {

        User user = userRepository.findByEmail(email).orElseThrow();

        return new UserDetailsImpl(user);
    }
}