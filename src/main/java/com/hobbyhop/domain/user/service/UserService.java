package com.hobbyhop.domain.user.service;

import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
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

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public void signup(SignupRequestDTO signupRequestDTO) {
        validateExistingUser(signupRequestDTO);

        User user = User.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
//                .role(signupRequestDTO.getRole())
                .build();
        userRepository.save(user);
    }

    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow();
        String username = user.getUsername();

        validatePassword(user, password);

        // accessToken 생성
        String accessToken = jwtUtil.createAccessToken(username);
        // accessToken 을 클라이언트에게 헤더로 넣어 보냄
        response.setHeader("Authorization", accessToken);
        // username을 key로, accessToken을 value로 -> redis에 저장
        jwtUtil.saveAccessTokenByUsername(username, accessToken);

        // refreshToken 생성
        String refreshToken = jwtUtil.createRefreshToken(username);
        // accessToken을 key로, refreshToken을 value로 -> redis에 저장
        jwtUtil.saveRefreshTokenByAccessToken(accessToken, refreshToken);
    }

    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = httpServletRequest.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        if (jwtUtil.validateToken(accessToken.substring(7))) {
            // request의 accessToken이 유효하다면 그대로 로그아웃 진행
            jwtUtil.removeRefreshToken(accessToken);
            jwtUtil.removeAccessToken(accessToken);
        } else {
            // 접근한 request의 accessToken이 유효하지 않다면 response에 새로운 accessToken을 발급했으므로 response에서 가져와서 로그아웃 진행
            String responseHeaderAccessToken = httpServletResponse.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            jwtUtil.removeRefreshToken(responseHeaderAccessToken);
            jwtUtil.removeAccessToken(responseHeaderAccessToken);
        }

        // response header에 token 반환하지 않도록
        httpServletResponse.setHeader(JwtUtil.AUTHORIZATION_HEADER, "logged-out");
    }

    @Transactional
    public void updateProfile(UpdateProfileDTO updateProfileDTO, UserDetailsImpl userDetails,
                              HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow();

        validatePassword(user, updateProfileDTO.getOldPassword());
        editComparison(updateProfileDTO);

        // Call the updateProfile method in the User entity
        user.updateProfile(updateProfileDTO.getUsername(), updateProfileDTO.getEmail(), updateProfileDTO.getConfirmPassword());

        // token의 subject를 username으로 발급했기 때문에
        // token을 바뀐 username으로 재발급
        String requestHeaderAccessToken = httpServletRequest.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername());
        if (jwtUtil.validateToken(requestHeaderAccessToken.substring(7))) {
            // request의 accessToken이 유효하다면 그대로
            jwtUtil.rebaseToken(newAccessToken, requestHeaderAccessToken);
        } else {
            // 접근한 request의 accessToken이 유효하지 않다면 response에 새로운 accessToken을 발급했으므로 response에서 가져와서
            String responseHeaderAccessToken = httpServletResponse.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            jwtUtil.rebaseToken(newAccessToken, responseHeaderAccessToken);
        }

        // 바뀐 username을 가진 토큰을 response에 반환
        httpServletResponse.setHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
    }

    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void editComparison(UpdateProfileDTO updateProfileDTO) {
        if (userRepository.existsByUsername(updateProfileDTO.getUsername())) {
            throw new IllegalArgumentException("username 이 수정 전과 같습니다.");
        }

        if (userRepository.existsByEmail(updateProfileDTO.getEmail())) {
            throw new IllegalArgumentException("email 이 수정 전과 같습니다.");
        }

        if (!updateProfileDTO.getNewPassword().equals(updateProfileDTO.getConfirmPassword())) {
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