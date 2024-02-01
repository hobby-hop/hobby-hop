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
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        User user = userRepository.findByEmail(email)
            .orElseThrow(NotFoundUserException::new);
        String username = user.getUsername();

        validatePassword(user, password);

        String accessToken = jwtUtil.createAccessToken(username);
        response.setHeader("Authorization", accessToken);
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
        String accessToken = httpServletRequest.getHeader(JwtUtil.AUTHORIZATION_HEADER);

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

        httpServletResponse.setHeader(jwtUtil.AUTHORIZATION_HEADER, "withdrawal");
    }

    @Override
    public MyProfileResponseDTO getMyProfile(UserDetailsImpl userDetails, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(userDetails.getUser().getId())
            .orElseThrow(NotFoundUserException::new);

        return MyProfileResponseDTO.fromEntity(user);
    }

    @Override
    public OtherProfileResponseDTO getOtherProfile(Long otherUserId, UserDetailsImpl userDetails, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(otherUserId)
            .orElseThrow(NotFoundUserException::new);

        return OtherProfileResponseDTO.fromEntity(user);
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileRequestDTO updateProfileRequestDTO, UserDetailsImpl userDetails,
        HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(userDetails.getUser().getId())
            .orElseThrow(NotFoundUserException::new);

        validatePassword(user, updateProfileRequestDTO.getOldPassword());
        if(updateProfileRequestDTO.getNewPassword() != null) {

           if (updateProfileRequestDTO.getNewPassword().equals(updateProfileRequestDTO.getOldPassword())) {
               throw new MatchedPasswordException();
           }

           if (!updateProfileRequestDTO.getNewPassword().equals(updateProfileRequestDTO.getConfirmPassword())) {
               throw new MismatchedNewPasswordException();
           }
           user.changePassword(passwordEncoder.encode(updateProfileRequestDTO.getNewPassword()));
        }
        if(updateProfileRequestDTO.getInfo() != null) {
            user.changeInfo(updateProfileRequestDTO.getInfo());
        }

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

    private void deletedUserVerification (SignupRequestDTO signupRequestDTO) {
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


}