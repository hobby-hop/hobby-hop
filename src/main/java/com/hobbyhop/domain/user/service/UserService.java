package com.hobbyhop.domain.user.service;

import com.hobbyhop.domain.user.constant.UserRoleEnum;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.global.exception.user.*;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
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

    public void signup(SignupRequestDTO signupRequestDTO) {
        validateExistingUser(signupRequestDTO);

        User user = User.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
                .role(UserRoleEnum.USER)
                .build();
        userRepository.save(user);
    }

    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundUser::new);
        String username = user.getUsername();

        validatePassword(user, password);

        String accessToken = jwtUtil.createAccessToken(username);
        response.setHeader("Authorization", accessToken);
        jwtUtil.saveAccessTokenByUsername(username, accessToken);

        String refreshToken = jwtUtil.createRefreshToken(username);
        jwtUtil.saveRefreshTokenByAccessToken(accessToken, refreshToken);
    }

    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = httpServletRequest.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        if (jwtUtil.validateToken(accessToken.substring(7))) {
            jwtUtil.removeRefreshToken(accessToken);
            jwtUtil.removeAccessToken(accessToken);
        } else {
            String responseHeaderAccessToken = httpServletResponse.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            jwtUtil.removeRefreshToken(responseHeaderAccessToken);
            jwtUtil.removeAccessToken(responseHeaderAccessToken);
        }
        httpServletResponse.setHeader(JwtUtil.AUTHORIZATION_HEADER, "logged-out");
    }

    @Transactional
    public void updateProfile(UpdateProfileDTO updateProfileDTO, UserDetailsImpl userDetails,
                              HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(NotFoundUser::new);

        validatePassword(user, updateProfileDTO.getOldPassword());
        editComparison(updateProfileDTO);
        String newPassword = passwordEncoder.encode(updateProfileDTO.getNewPassword());

        user.updateProfile(updateProfileDTO.getUsername(), updateProfileDTO.getEmail(), newPassword);

        String requestHeaderAccessToken = httpServletRequest.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername());
        if (jwtUtil.validateToken(requestHeaderAccessToken.substring(7))) {
            jwtUtil.rebaseToken(newAccessToken, requestHeaderAccessToken);
        } else {
            String responseHeaderAccessToken = httpServletResponse.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            jwtUtil.rebaseToken(newAccessToken, responseHeaderAccessToken);
        }
        httpServletResponse.setHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
    }

    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new MismatchedPassword();
        }
    }

    private void editComparison(UpdateProfileDTO updateProfileDTO) {
        if (userRepository.existsByUsername(updateProfileDTO.getUsername())) {
            throw new UsernameUnchanged();
        }

        if (userRepository.existsByEmail(updateProfileDTO.getEmail())) {
            throw new EmailUnchanged();
        }

        if (!updateProfileDTO.getNewPassword().equals(updateProfileDTO.getConfirmPassword())) {
            throw new MismatchedNewPassword();
        }
    }

    private void validateExistingUser(SignupRequestDTO signupRequestDTO) {
        if (userRepository.findByUsername((signupRequestDTO.getUsername())).isPresent()) {
            throw new AlreadyExistUsername();
        }

        if (userRepository.findByEmail((signupRequestDTO.getEmail())).isPresent()) {
            throw new AlreadyExistEmail();
        }

        if (!signupRequestDTO.getConfirmPassword().equals(signupRequestDTO.getPassword())) {
            throw new MismatchedPassword();
        }
    }
}