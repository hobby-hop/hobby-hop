package com.hobbyhop.global.security.auth;

import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class AuthController {
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    @PostMapping("/signup")
//    public ResponseEntity<ApiResponseDTO> signup (@Valid @RequestBody SignupRequestDTO userRequestDto) {
//        try {
//            userService.signup(userRequestDto);
//        } catch (IllegalArgumentException exception) {
//            return ResponseEntity.badRequest().body(new ApiResponseDTO("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
//        }
//        return ResponseEntity.status(HttpStatus.CREATED.value())
//                .body(new ApiResponseDTO("회원가입 성공", HttpStatus.CREATED.value()));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponseDTO> login (@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
//        try {
//            userService.login(loginRequestDTO);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(new ApiResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
//        }
//        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDTO.getUsername()));
//
//        return ResponseEntity.ok().body(new ApiResponseDTO("로그인 성공", HttpStatus.OK.value()));
//    }
//}