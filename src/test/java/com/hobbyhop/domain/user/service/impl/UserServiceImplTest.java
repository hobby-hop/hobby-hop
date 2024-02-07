package com.hobbyhop.domain.user.service.impl;

import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.global.exception.user.NotAvailableUsernameException;
import com.hobbyhop.global.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        // 아직 뭐 넣어야 될지 모르겠음...
    }

    @Test
    @DisplayName("회원 가입 성공")
    void testUserSignupSuccess() {
        // Given
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setUsername("kkamjjing");
        signupRequestDTO.setEmail("test@example.com");
        signupRequestDTO.setPassword("password");
        signupRequestDTO.setConfirmPassword("password");

        when(userRepository.existsByEmailAndDeletedAtIsNull(signupRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByEmailAndDeletedAtIsNotNull(signupRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsernameAndDeletedAtIsNull(signupRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByUsernameAndDeletedAtIsNotNull(signupRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByUsername(signupRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequestDTO.getEmail())).thenReturn(false);

        // When
        userService.signup(signupRequestDTO);

        // Then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test user signup with existing username")
    void testUserSignupWithExistingUsername() {
        // Given
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setUsername("existingUser");
        signupRequestDTO.setEmail("test@example.com");
        signupRequestDTO.setPassword("password");
        signupRequestDTO.setConfirmPassword("password");

        when(userRepository.existsByUsername(signupRequestDTO.getUsername())).thenReturn(true);

        // When, Then
        assertThatThrownBy(() -> userService.signup(signupRequestDTO))
                .isInstanceOf(NotAvailableUsernameException.class);
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void getMyProfile() {
    }

    @Test
    void getOtherProfile() {
    }

    @Test
    void updateProfile() {
    }
}