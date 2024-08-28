package com.hobbyhop.domain.user.controller;

import com.hobbyhop.domain.user.dto.*;
import com.hobbyhop.domain.user.service.SocialService;
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
    private final SocialService socialService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<?> signup(
            @Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);

        return ApiResponse.ok("회원가입 성공");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
                                HttpServletResponse response) {
        userService.login(loginRequestDTO, response);

        return ApiResponse.ok("로그인 성공");
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> logout(HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) {
        userService.logout(httpServletRequest, httpServletResponse);

        return ApiResponse.ok("로그아웃 성공");
    }

    @Operation(summary = "회원 탈퇴")
    @PostMapping("/withdrawal")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> withdrawal(@Valid @RequestBody WithdrawalRequestDTO withdrawalRequestDTO,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {
        userService.withdraw(withdrawalRequestDTO, httpServletRequest, httpServletResponse);

        return ApiResponse.ok("회원 탈퇴 성공");
    }

    @Operation(summary = "자신 프로필 조회")
    @GetMapping("/profiles/my")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userService.getMyProfile(userDetails));
    }

    @Operation(summary = "다른 유저 프로필 조회")
    @GetMapping("/profiles/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> getMyProfile(@PathVariable("userId") Long userId) {
        return ApiResponse.ok(userService.getOtherProfile(userId));
    }

    @Operation(summary = "내 프로필 수정")
    @PatchMapping("/profiles")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> update(@Valid @RequestBody UpdateProfileRequestDTO updateProfileRequestDTO,
                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                HttpServletResponse httpServletResponse,
                                HttpServletRequest httpServletRequest) {
        userService.updateProfile(updateProfileRequestDTO, userDetails, httpServletResponse, httpServletRequest);

        return ApiResponse.ok("사용자 정보 수정 성공");
    }

    @Operation(summary = "카카오 로그인")
    @GetMapping("/login/kakao/callback")
    public ApiResponse<?> kakaoLogin(@RequestParam String code,
                                     HttpServletResponse response) {
        socialService.socialLogin(code, response);

        return ApiResponse.ok("카카오 로그인 성공");
    }
}