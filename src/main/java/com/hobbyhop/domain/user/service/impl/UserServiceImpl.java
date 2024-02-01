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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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
    public MyProfileResponseDTO getMyProfile(UserDetailsImpl userDetails, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = getUserById(userDetails.getUser().getId());
        return MyProfileResponseDTO.fromEntity(user);
    }

    @Override
    public OtherProfileResponseDTO getOtherProfile(Long otherUserId, UserDetailsImpl userDetails, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = getUserById(userDetails.getUser().getId());
        return OtherProfileResponseDTO.fromEntity(user);
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileRequestDTO updateProfileRequestDTO, UserDetailsImpl userDetails, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = getUserById(userDetails.getUser().getId());
        validatePassword(user, updateProfileRequestDTO.getOldPassword());
        // 비밀번호 변경
        if (updateProfileRequestDTO.getNewPassword() != null) {  // 새 비밀번호가 null 이 아닐 경우
            validateNewPassword(updateProfileRequestDTO.getOldPassword(), updateProfileRequestDTO.getNewPassword(), updateProfileRequestDTO.getConfirmPassword());
            user.changePassword(passwordEncoder.encode(updateProfileRequestDTO.getNewPassword()));
        }
        // 자기소개 변경
        if (updateProfileRequestDTO.getInfo() != null) {
            user.changeInfo(updateProfileRequestDTO.getInfo());
        }
        // 토큰 재발급
        updateAccessToken(httpServletRequest, httpServletResponse, user);
    }

    //===================================================================================================
    // 공통 메서드 1 - 유저 저장
    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }

    // 공통 메서드 2 - 비밀번호 일치하는지 확인
    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new MismatchedPasswordException();
        }
    }

    // 회원가입 메서드 1 - 회원가입 빌더
    private User signupUser(SignupRequestDTO signupRequestDTO) {
        return User.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
                .info(signupRequestDTO.getInfo())
                .role(UserRoleEnum.USER)
                .build();
    }

    // 회원가입 메서드 2 - 탈퇴한 사용자인지 확인
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

    // 회원가입 메서드 3 - 존재하는 유저인지 확인
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

    // 로그아웃 메서드
    private void processToken(String token) {
        if (token != null && jwtUtil.validateToken(token.substring(7))) {
            jwtUtil.removeAccessToken(token);
            jwtUtil.removeRefreshToken(token);
        }
    }

    // 유저 정보 수정 메서드 1  - 비밀번호 변경
    private void validateNewPassword(String oldPassword, String newPassword, String confirmPassword) {
        if (newPassword.equals(oldPassword)) { // 새 비밀번호와 예전 비밀번호가 같다면 예외 처리
            throw new MatchedPasswordException();
        }

        if (!newPassword.equals(confirmPassword)) { // 새 비밀번호와 확인용 비밀번호가 다르다면 예외 처리
            throw new MismatchedNewPasswordException();
        }
    }

    // 유저 정보 수정 메서드 2 - 액세스 토큰 새로 발급
    private void updateAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, User user) {
        String requestHeaderAccessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER); // 현재 액세스 토큰을 헤더에서 가져오기
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername()); // username 으로 새 액세스 토큰 생성

        if (jwtUtil.validateToken(requestHeaderAccessToken.substring(7))) { // 현재 액세스 토큰이 유효한지 확인
            jwtUtil.rebaseToken(newAccessToken, requestHeaderAccessToken); // 유효하면 새 액세스 토큰으로 업데이트
        } else {
            String responseHeaderAccessToken = httpServletResponse.getHeader(AUTHORIZATION_HEADER);
            jwtUtil.rebaseToken(newAccessToken, responseHeaderAccessToken); // 유효하지 않으면 헤더 액세스 토큰으로 업데이트
        }
        httpServletResponse.setHeader(AUTHORIZATION_HEADER, newAccessToken); // 헤더에 새 액세스 토큰 set하기
    }
}