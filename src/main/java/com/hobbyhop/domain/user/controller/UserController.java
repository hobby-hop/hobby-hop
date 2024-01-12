package com.hobbyhop.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.domain.user.service.KakaoService;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup (
            @Valid
            @RequestBody SignupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(
                "회원가입 성공"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login (
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {
        userService.login(loginRequestDTO, response);
        return ResponseEntity.ok(ApiResponse.ok(
                "로그인 성공"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse httpServletResponse) {
        userService.logout(httpServletResponse);
        return ResponseEntity.ok(ApiResponse.ok(
                "로그아웃 성공"
        ));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> update (
            @RequestBody UpdateProfileDTO updateProfileDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateProfile(updateProfileDTO, userDetails);
        return ResponseEntity.ok(ApiResponse.ok(
                "사용자 정보 수정 성공"
        ));
    }

    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code, response);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}