package com.hobbyhop.domain.user.controller;

import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    @PatchMapping("/update") // {userId} 안 받아도 되나요? API 명세에는 없습니다. 그리고 프로필 조회 API 는 없어도 되나요?
    public ResponseEntity<ApiResponse> update (
            @RequestBody UpdateProfileDTO updateProfileDTO,
            UserDetailsImpl userDetails) {
        userService.updateProfile(updateProfileDTO, userDetails);
        return ResponseEntity.ok(ApiResponse.ok(
                "사용자 정보 수정 성공"
        ));
    }
}