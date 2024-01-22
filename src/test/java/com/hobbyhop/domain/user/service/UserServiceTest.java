//package com.hobbyhop.domain.user.service;
//
//import com.hobbyhop.domain.user.dto.LoginRequestDTO;
//import com.hobbyhop.domain.user.dto.SignupRequestDTO;
//import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
//import com.hobbyhop.domain.user.entity.User;
//import com.hobbyhop.domain.user.repository.UserRepository;
//import com.hobbyhop.domain.user.service.UserService;
//import com.hobbyhop.global.security.jwt.JwtUtil;
//import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Test
//    void testSignup() {
//        // 주어진 상황
//        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
//        signupRequestDTO.setUsername("testuser");
//        signupRequestDTO.setEmail("testuser@example.com");
//        signupRequestDTO.setPassword("testpassword");
//        signupRequestDTO.setConfirmPassword("testpassword");
//
//        // 실행
//        userService.signup(signupRequestDTO);
//
//        // 확인
//        User user = userRepository.findByUsername("testuser").orElse(null);
//        assertNotNull(user);
//        assertEquals("testuser", user.getUsername());
//        assertEquals("testuser@example.com", user.getEmail());
//        assertTrue(passwordEncoder.matches("testpassword", user.getPassword()));
//    }
//
//    @Test
//    void testLogin() {
//        // 주어진 상황
//        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
//        signupRequestDTO.setUsername("testuser");
//        signupRequestDTO.setEmail("testuser@example.com");
//        signupRequestDTO.setPassword("testpassword");
//        signupRequestDTO.setConfirmPassword("testpassword");
//        userService.signup(signupRequestDTO);
//
//        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
//        loginRequestDTO.setEmail("testuser@example.com");
//        loginRequestDTO.setPassword("testpassword");
//
//        // 실행
//        userService.login(loginRequestDTO, null);
//
//        // 확인: 예외가 발생하지 않으면 로그인 성공
//    }
//
//    @Test
//    void testUpdateProfile() {
//        // 주어진 상황
//        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
//        signupRequestDTO.setUsername("testuser");
//        signupRequestDTO.setEmail("testuser@example.com");
//        signupRequestDTO.setPassword("testpassword");
//        signupRequestDTO.setConfirmPassword("testpassword");
//        userService.signup(signupRequestDTO);
//
//        User user = userRepository.findByUsername("testuser").orElse(null);
//        assertNotNull(user);
//
//        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
//        updateProfileDTO.setUsername("newusername");
//        updateProfileDTO.setEmail("newemail@example.com");
//        updateProfileDTO.setOldPassword("testpassword");
//        updateProfileDTO.setNewPassword("newpassword");
//        updateProfileDTO.setConfirmPassword("newpassword");
//
//        // 실행
//        userService.updateProfile(updateProfileDTO, new UserDetailsImpl(user), null, null);
//
//        // 확인
//        User updatedUser = userRepository.findByUsername("newusername").orElse(null);
//        assertNotNull(updatedUser);
//        assertEquals("newusername", updatedUser.getUsername());
//        assertEquals("newemail@example.com", updatedUser.getEmail());
//        assertTrue(passwordEncoder.matches("newpassword", updatedUser.getPassword()));
//    }
//}