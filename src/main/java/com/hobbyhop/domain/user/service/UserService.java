package com.hobbyhop.domain.user.service;

import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public void signup(SignupRequestDTO signupRequestDTO) {
        validateExistingUser(signupRequestDTO);

        User user = User.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
                .build();

        userRepository.save(user);
    }

    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        validatePassword(user, password);

        response.setHeader("Authorization", jwtUtil.createToken(username));
    }

    public void logout(HttpServletResponse response) {
        response.setHeader("Authorization", jwtUtil.createToken(null));
    }

    @Transactional
    public void updateProfile(UpdateProfileDTO updateProfileDTO, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow();

        String updateUsername = updateProfileDTO.getUsername();
        String updateEmail = updateProfileDTO.getEmail();
        String confirmPassword = updateProfileDTO.getConfirmPassword();
        //        String introduce = updateProfileDTO.introduce();

        validatePassword(user, updateProfileDTO.getOldPassword());
        editComparison(updateProfileDTO);
        user.updateProfile(updateUsername, updateEmail, confirmPassword);

        // 1. updateProfileDTO.getUsername() 이 user 에 있는 username 과 같은지 확인했을 때 다르다면 username 을 수정하기
        // 2. updateProfileDTO.getEmail() 이 user 에 있는 email 과 같은지 확인했을 때 다르다면 email 을 수정하기
        // 3-1. updateProfileDTO.getOldPassword() 가 user 에 있는 password 와 같은지 확인했을 때 같고,
        // 3-2. updateProfileDTO.getNewPassword() 가 updateProfileDTO.getConfirmPassword() 와 같은지 확인했을 때 같다면 Password 를 수정하기
    }

    //----------------------

    private void validatePassword (User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void editComparison (UpdateProfileDTO updateProfileDTO) {
        if (userRepository.existsByUsername(updateProfileDTO.getUsername())) {
            throw new IllegalArgumentException("username 이 수정 전과 같습니다.");
        }

        if (userRepository.existsByEmail(updateProfileDTO.getEmail())) {
            throw new IllegalArgumentException("email 이 수정 전과 같습니다.");
        }

        if (! updateProfileDTO.getNewPassword().equals(updateProfileDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateExistingUser (SignupRequestDTO signupRequestDTO) {
        if(userRepository.findByUsername((signupRequestDTO.getUsername())).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        if(userRepository.findByEmail((signupRequestDTO.getEmail())).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (! signupRequestDTO.getConfirmPassword().equals(signupRequestDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}