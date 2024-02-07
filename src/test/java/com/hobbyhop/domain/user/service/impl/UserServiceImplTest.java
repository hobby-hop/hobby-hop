package com.hobbyhop.domain.user.service.impl;

import com.hobbyhop.domain.user.dto.*;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.global.exception.user.MismatchedPasswordException;
import com.hobbyhop.global.exception.user.NotAvailableUsernameException;
import com.hobbyhop.global.exception.user.NotFoundUserException;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    private static final String AUTHORIZATION_HEADER = JwtUtil.AUTHORIZATION_HEADER;
    // 아래서 이게 들어가야 하는 부분에 어떻게 넣어줘야 에러가 안 나는지 잘 모르겠음... 그리고 Claims 는 어떻게 주입해야 하는 건지도 모르겠음...

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
    @DisplayName("로그인 성공")
    void testUserLoginSuccess() {
        // Given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("password");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("$2a$10$wF/wBC4s2gixrL4t.9RSt.wCzU/SHRUMR7e7oGV1Gc32A9sJrE1XO"); // 암호화한 비밀번호

        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())).thenReturn(true);

        when(jwtUtil.createAccessToken(user.getUsername())).thenReturn("testAccessToken");
        when(jwtUtil.createRefreshToken(user.getUsername())).thenReturn("testRefreshToken");

        // When
        userService.login(loginRequestDTO, response);

        // Then
        verify(response, times(1)).setHeader(eq("Authorization"), eq("testAccessToken"));
        verify(jwtUtil, times(1)).saveAccessTokenByUsername(eq(user.getUsername()), eq("testAccessToken"));
        verify(jwtUtil, times(1)).saveRefreshTokenByAccessToken(eq("testAccessToken"), eq("testRefreshToken"));
    }

    @Test
    @DisplayName("로그인하려는 유저의 비밀번호가 일치하지 않을 때")
    void testUserLoginWithIncorrectPassword() {
        // Given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("incorrectPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("$2a$10$wF/wBC4s2gixrL4t.9RSt.wCzU/SHRUMR7e7oGV1Gc32A9sJrE1XO"); // 암호화한 비밀번호

        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(loginRequestDTO, response))
                .isInstanceOf(MismatchedPasswordException.class);
    }

    @Test
    @DisplayName("로그인하려는 유저의 이메일이 존재하지 않을 때")
    void testUserLoginWithNonExistentEmail() {
        // Given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("nonexistent@example.com");
        loginRequestDTO.setPassword("password");

        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> userService.login(loginRequestDTO, response))
                .isInstanceOf(NotFoundUserException.class);
    }


//    @Test
//    @DisplayName("로그아웃 성공")
//    void testUserLogout() {
//        // Given
//        String accessToken = "testAccessToken";
//
//        when(httpServletRequest.getHeader(UserServiceImpl.AUTHORIZATION_HEADER)).thenReturn(accessToken);
//
//        // When
//        userService.logout(httpServletRequest, httpServletResponse);
//
//        // Then
//        verify(httpServletRequest, times(1)).getHeader(UserServiceImpl.AUTHORIZATION_HEADER);
//        verify(httpServletResponse, times(1)).setHeader(UserServiceImpl.AUTHORIZATION_HEADER, "logged-out");
//    }

//    @Test
//    @DisplayName("회원 탈퇴 성공")
//    void testUserWithdrawal() {
//        // Given
//        String accessToken = "testAccessToken";
//        WithdrawalRequestDTO withdrawalRequestDTO = new WithdrawalRequestDTO();
//        withdrawalRequestDTO.setPassword("password");
//
//        when(httpServletRequest.getHeader(UserServiceImpl.AUTHORIZATION_HEADER)).thenReturn(accessToken);
//
//        when(jwtUtil.validateToken(accessToken.substring(7))).thenReturn(true);
//        Claims claims = JwtUtil.claims(); // 이걸 어떻게 넣지...?!?!
//        claims.setSubject("testUser");
//        when(jwtUtil.getUserInfo(accessToken.substring(7))).thenReturn(claims);
//
//        User user = new User();
//        user.setUsername("testUser");
//        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//
//        // When
//        userService.withdraw(withdrawalRequestDTO, httpServletRequest, httpServletResponse);
//
//        // Then
//        verify(httpServletRequest, times(1)).getHeader(UserServiceImpl.AUTHORIZATION_HEADER);
//        verify(jwtUtil, times(1)).removeAccessToken(accessToken);
//        verify(jwtUtil, times(1)).removeRefreshToken(accessToken);
//        verify(userRepository, times(1)).delete(user);
//        verify(httpServletResponse, times(1)).setHeader(UserServiceImpl.AUTHORIZATION_HEADER, "withdrawal");
//    }

    @Test
    @DisplayName("내 프로필 조회 성공")
    void testUserProfileView() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // When
        when(userDetails.getUser()).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.getMyProfile(userDetails, httpServletResponse, httpServletRequest);

        // Then
        verify(userRepository, times(1)).findById(userId);
    }

//    @Test
//    @DisplayName("다른 사용자 프로필 조회 성공") // 실행하면 에러 남...
//    void testOtherUserProfileView() {
//        // Given
//        Long otherUserId = 3L;
//        Long currentUserId = 1L;
//
//        User otherUser = new User();
//        otherUser.setId(otherUserId);
//        otherUser.setUsername("otherUser");
//        otherUser.setInfo("다른 유저의 자기 소개");
//
//        // Mock UserDetailsImpl behavior
//        User currentUser = new User();
//        currentUser.setId(currentUserId);
//        when(userDetails.getUser()).thenReturn(currentUser);
//
//        // Mock UserRepository behavior
//        when(userRepository.findById(anyLong())).thenAnswer(invocation -> {
//            Long id = invocation.getArgument(0);
//            assertThat(id).isEqualTo(otherUserId); // Verify the argument
//            return Optional.of(otherUser);
//        });
//
//        // When
//        OtherProfileResponseDTO responseDTO = userService.getOtherProfile(otherUserId, userDetails, httpServletResponse, httpServletRequest);
//
//        // Then
//        assertThat(responseDTO).isNotNull();
//        assertThat(responseDTO.getUsername()).isEqualTo("otherUser");
//        assertThat(responseDTO.getInfo()).isEqualTo("다른 유저의 자기 소개");
//    }

//    @Test
//    @DisplayName("내 프로필 수정 성공")
//    void testProfileUpdateSuccess() {
//        // Given
//        UpdateProfileRequestDTO updateProfileRequestDTO = new UpdateProfileRequestDTO();
//        updateProfileRequestDTO.setInfo("New info");
//        updateProfileRequestDTO.setOldPassword("oldPassword");
//        updateProfileRequestDTO.setNewPassword("newPassword");
//        updateProfileRequestDTO.setConfirmPassword("newPassword");
//
//        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
//
//        User mockUser = new User();
//        mockUser.setId(1L);
//        mockUser.setPassword("oldPassword"); // Assuming old password matches
//        given(userRepository.findById(any())).willReturn(Optional.of(mockUser));
//        given(passwordEncoder.matches(any(), any())).willReturn(true);
//        given(passwordEncoder.encode(any())).willReturn("encodedPassword");
//
//        // when
//        userService.updateProfile(updateProfileRequestDTO, userDetails, httpServletResponse, httpServletRequest);
//
//        // then
//        verify(userRepository).findById(any());
//        verify(passwordEncoder).matches(any(), any());
//        verify(passwordEncoder).encode(any());
//    }

//    @Test
//    @DisplayName("일치하지 않는 비밀번호")
//    void testProfileUpdateWithMismatchedPassword() {
//        // given
//        UpdateProfileRequestDTO updateProfileRequestDTO = new UpdateProfileRequestDTO();
//        updateProfileRequestDTO.setInfo("New info");
//        updateProfileRequestDTO.setOldPassword("oldPassword");
//        updateProfileRequestDTO.setNewPassword("newPassword");
//        updateProfileRequestDTO.setConfirmPassword("newPassword");
//
//        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
//
//        User mockUser = new User();
//        mockUser.setId(1L);
//        mockUser.setPassword("oldPassword");
//        given(userRepository.findById(any())).willReturn(Optional.of(mockUser));
//        given(passwordEncoder.matches(any(), any())).willReturn(false);
//
//        // when, then
//        assertThrows(MismatchedPasswordException.class,
//                () -> userService.updateProfile(updateProfileRequestDTO, userDetails, httpServletResponse, httpServletRequest));
//    }
}