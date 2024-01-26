package com.hobbyhop.domain.user.controller;

import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.domain.user.service.KakaoService;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<?> signup (
            @Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);
        return ApiResponse.ok(
                "회원가입 성공"
        );
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<?> login (
            @Valid @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {
        userService.login(loginRequestDTO, response);
        return ApiResponse.ok(
                "로그인 성공"
        );
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<?> logout (
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        userService.logout(httpServletRequest, httpServletResponse);
        return ApiResponse.ok(
                "로그아웃 성공"
        );
    }

    // 내가 내 정보 조회했을 때 보여야 하는 것 : username, email, password, introduce, 가입한 모임 리스트, 내가 쓴 게시물 리스트, 사이트 회원 탈퇴
    // 내가 다른 유저 정보 조회했을 때 보여야 하는 것 : username, introduce

    @Operation(summary = "내 프로필 조회")
    @GetMapping ("/myprofile") // {userId} 넣어야 하나...?
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> getMyProfile (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {
        userService.getMyProfile(userDetails, httpServletResponse, httpServletRequest);
        return ApiResponse.ok(
                "내 프로필 조회 성공"
        );
    }

    @Operation(summary = "내 프로필 수정")
    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> update(
            @Valid @RequestBody UpdateProfileDTO updateProfileDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {
        userService.updateProfile(updateProfileDTO, userDetails, httpServletResponse,
                httpServletRequest);
        return ApiResponse.ok(
                "사용자 정보 수정 성공"
        );
    }

    @Operation(summary = "카카오 로그인")
    @GetMapping("/login/kakao/callback")
    public ApiResponse<?> kakaoLogin(
            @RequestParam String code,
            HttpServletResponse response) {
        kakaoService.kakaoLogin(code, response);
        return ApiResponse.ok(
                "카카오 로그인 성공"
        );
    }
}