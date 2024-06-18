package com.hobbyhop.domain.user.controller;

import com.hobbyhop.domain.user.dto.*;
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
    public ApiResponse<?> signup(
            @Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);

        return ApiResponse.ok("회원가입 성공");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<?> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {
        userService.login(loginRequestDTO, response);

        return ApiResponse.ok("로그인 성공");
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> logout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        userService.logout(httpServletRequest, httpServletResponse);

        return ApiResponse.ok("로그아웃 성공");
    }

    @Operation(summary = "회원 탈퇴")
    @PostMapping("/withdrawal")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> withdrawal(
            @Valid @RequestBody WithdrawalRequestDTO withdrawalRequestDTO,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        userService.withdraw(withdrawalRequestDTO, httpServletRequest, httpServletResponse);

        return ApiResponse.ok("회원 탈퇴 성공");
    }

    @Operation(summary = "내 프로필 조회")
    @GetMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {
        MyProfileResponseDTO myProfileResponseDTO = userService.getMyProfile(userDetails, httpServletResponse, httpServletRequest);

        return ApiResponse.ok(myProfileResponseDTO);
    }

    @Operation(summary = "다른 유저 프로필 조회")
    @GetMapping("/profile/{otherUserId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> getOtherProfile(
            @PathVariable Long otherUserId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {
        OtherProfileResponseDTO otherProfileResponseDTO = userService.getOtherProfile(otherUserId, userDetails, httpServletResponse, httpServletRequest);

        return ApiResponse.ok(otherProfileResponseDTO);
    }

    @Operation(summary = "내 프로필 수정")
    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> update(
            @Valid @RequestBody UpdateProfileRequestDTO updateProfileRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {
        userService.updateProfile(updateProfileRequestDTO, userDetails, httpServletResponse,
                httpServletRequest);

        return ApiResponse.ok("사용자 정보 수정 성공");
    }

    @Operation(summary = "카카오 로그인")
    @GetMapping("/login/kakao/callback")
    public ApiResponse<?> kakaoLogin(
            @RequestParam String code,
            HttpServletResponse response) {
        kakaoService.kakaoLogin(code, response);

        return ApiResponse.ok("카카오 로그인 성공");
    }
}