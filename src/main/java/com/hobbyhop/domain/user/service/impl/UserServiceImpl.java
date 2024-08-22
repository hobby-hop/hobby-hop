package com.hobbyhop.domain.user.service.impl;

import com.hobbyhop.domain.user.dto.*;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.enums.UserRoleEnum;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.exception.jwt.InvalidJwtException;
import com.hobbyhop.global.exception.user.*;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String AUTHORIZATION_HEADER = JwtUtil.AUTHORIZATION_HEADER;

    @Override
    public void signup(SignupRequestDTO signupRequestDTO) {
        try {
            withdrawnUserVerification(signupRequestDTO);
            validateExistingUser(signupRequestDTO);
            User user = signupUser(signupRequestDTO);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException();
        }
    }

    @Override
    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(NotFoundUserException::new);
        String username = user.getUsername();
        validatePassword(user, loginRequestDTO.getPassword());

        String accessToken = jwtUtil.createAccessToken(username);
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        jwtUtil.saveAccessTokenByUsername(username, accessToken);

        String refreshToken = jwtUtil.createRefreshToken(username);
        jwtUtil.saveRefreshTokenByAccessToken(accessToken, refreshToken);
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        processToken(accessToken);

        String responseHeaderAccessToken = httpServletResponse.getHeader(AUTHORIZATION_HEADER);
        processToken(responseHeaderAccessToken);

        httpServletResponse.setHeader(AUTHORIZATION_HEADER, "logged-out");
    }

    @Override
    public void withdraw(WithdrawalRequestDTO withdrawalRequestDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (accessToken == null || !jwtUtil.validateToken(accessToken.substring(7))) {
            throw new InvalidJwtException();
        }
        Claims claims = jwtUtil.getUserInfo(accessToken.substring(7));
        String username = claims.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(NotFoundUserException::new);
        validatePassword(user, withdrawalRequestDTO.getPassword());

        jwtUtil.removeAccessToken(accessToken);
        jwtUtil.removeRefreshToken(accessToken);
        userRepository.delete(user);

        httpServletResponse.setHeader(AUTHORIZATION_HEADER, "withdrawal");
    }

    @Override
    public ProfileResponseDTO getMyProfile(UserDetailsImpl userDetails) {
        return ProfileResponseDTO.fromEntity(userDetails.getUser());

    }

    @Override
    public ProfileResponseDTO getOtherProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);

        return ProfileResponseDTO.fromEntity(user);
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileRequestDTO updateProfileRequestDTO, UserDetailsImpl userDetails, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(NotFoundUserException::new);
        validatePassword(user, updateProfileRequestDTO.getOldPassword());

        if (updateProfileRequestDTO.getNewPassword() != null) {
            validateNewPassword(updateProfileRequestDTO.getOldPassword(), updateProfileRequestDTO.getNewPassword(), updateProfileRequestDTO.getConfirmPassword());
            user.changePassword(passwordEncoder.encode(updateProfileRequestDTO.getNewPassword()));
        }

        if (updateProfileRequestDTO.getInfo() != null) {
            user.changeInfo(updateProfileRequestDTO.getInfo());
        }

        updateAccessToken(httpServletRequest, httpServletResponse, user);
    }



    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new MismatchedPasswordException();
        }
    }

    private User signupUser(SignupRequestDTO signupRequestDTO) {
        return User.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
                .info(signupRequestDTO.getInfo())
                .role(UserRoleEnum.USER)
                .build();
    }

    private void withdrawnUserVerification(SignupRequestDTO signupRequestDTO) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(signupRequestDTO.getEmail())) {
            throw new AlreadyExistEmailException();
        }

        if (userRepository.existsByEmailAndDeletedAtIsNotNull(signupRequestDTO.getEmail())) {
            throw new NotAvailableEmailException();
        }

        if (userRepository.existsByUsernameAndDeletedAtIsNull(signupRequestDTO.getUsername())) {
            throw new AlreadyExistUsernameException();
        }

        if (userRepository.existsByUsernameAndDeletedAtIsNotNull(signupRequestDTO.getUsername())) {
            throw new NotAvailableUsernameException();
        }
    }

    private void validateExistingUser(SignupRequestDTO signupRequestDTO) {
        if (userRepository.existsByUsername(signupRequestDTO.getUsername())) {
            throw new NotAvailableUsernameException();
        }

        if (userRepository.existsByEmail(signupRequestDTO.getEmail())) {
            throw new NotAvailableEmailException();
        }

        if (!signupRequestDTO.getConfirmPassword().equals(signupRequestDTO.getPassword())) {
            throw new MismatchedPasswordException();
        }
    }

    private void processToken(String token) {
        if (token != null && jwtUtil.validateToken(token.substring(7))) {
            jwtUtil.removeAccessToken(token);
            jwtUtil.removeRefreshToken(token);
        }
    }

    private void validateNewPassword(String oldPassword, String newPassword, String confirmPassword) {
        if (newPassword.equals(oldPassword)) {
            throw new MatchedPasswordException();
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new MismatchedNewPasswordException();
        }
    }

    private void updateAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, User user) {
        String requestHeaderAccessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername());

        if (jwtUtil.validateToken(requestHeaderAccessToken.substring(7))) {
            jwtUtil.rebaseToken(newAccessToken, requestHeaderAccessToken);
        } else {
            String responseHeaderAccessToken = httpServletResponse.getHeader(AUTHORIZATION_HEADER);
            jwtUtil.rebaseToken(newAccessToken, responseHeaderAccessToken);
        }

        httpServletResponse.setHeader(AUTHORIZATION_HEADER, newAccessToken);
    }
}