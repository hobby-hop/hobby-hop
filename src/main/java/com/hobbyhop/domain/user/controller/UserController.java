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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<?> signup(
            @Valid
            @RequestBody SignupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);
        return ApiResponse.ok(
                "회원가입 성공"
        );
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<?> login(
            @Valid
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {
        userService.login(loginRequestDTO, response);
        return ApiResponse.ok(
                "로그인 성공"
        );
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        userService.logout(httpServletRequest, httpServletResponse);
        return ApiResponse.ok(
                "로그아웃 성공"
        );
    }

    @Operation(summary = "사용자 정보 수정")
    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> update(
            @Valid
            @RequestBody UpdateProfileDTO updateProfileDTO,
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
    public ApiResponse<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        kakaoService.kakaoLogin(code, response);
        return ApiResponse.ok(
                "카카오 로그인 성공"
        );
    }
}