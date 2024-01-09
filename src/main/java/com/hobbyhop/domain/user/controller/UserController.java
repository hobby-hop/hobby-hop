package com.hobbyhop.domain.user.controller;

import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup (@Valid @RequestBody SignupRequestDTO signupRequestDTO) {

        userService.signup(signupRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(
                "회원가입 성공"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login (@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        userService.login(loginRequestDTO, response);
        return ResponseEntity.ok(ApiResponse.ok(
                "로그인 성공"
        ));
    }
}