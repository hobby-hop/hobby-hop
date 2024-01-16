package com.hobbyhop.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.domain.user.service.KakaoService;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(
            @Valid
            @RequestBody SignupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(
                "회원가입 성공"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {
        userService.login(loginRequestDTO, response);
        return ResponseEntity.ok(ApiResponse.ok(
                "로그인 성공"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        userService.logout(httpServletRequest, httpServletResponse);
        return ResponseEntity.ok(ApiResponse.ok(
                "로그아웃 성공"
        ));
    }

    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse> update(
            @RequestBody UpdateProfileDTO updateProfileDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {
        log.info("컨트롤러단 진입");
        log.info("userDetails : " + userDetails);

        userService.updateProfile(updateProfileDTO, userDetails, httpServletResponse, httpServletRequest);
        return ResponseEntity.ok(ApiResponse.ok(
                "사용자 정보 수정 성공"
        ));
    }

    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=6666e4d3e7955e33eac0eb2e6609e3e5&redirect_uri=
    @GetMapping("/login/kakao/callback")
    public ResponseEntity<ApiResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response);

        return ResponseEntity.ok(ApiResponse.ok(
                "카카오 로그인 성공"
        ));
    }

}