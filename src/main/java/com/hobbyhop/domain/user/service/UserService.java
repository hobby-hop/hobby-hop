package com.hobbyhop.domain.user.service;

import com.hobbyhop.domain.user.dto.UpdateProfileDTO;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import com.hobbyhop.domain.user.dto.LoginRequestDTO;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public void signup(SignupRequestDTO signupRequestDTO) {

        validateExistingUser(signupRequestDTO);

        User user = User.builder()
                .username(signupRequestDTO.getUsername())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .email(signupRequestDTO.getEmail())
                .build();

        userRepository.save(user);
    }
    private void validateExistingUser(SignupRequestDTO signupRequestDTO) {

        if(userRepository.findByUsername((signupRequestDTO.getUsername())).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        if(userRepository.findByEmail((signupRequestDTO.getEmail())).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (signupRequestDTO.getConfirmPassword() != signupRequestDTO.getPassword()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        if (!passwordEncoder.matches(password, user.getPassword())) {

        }

        response.setHeader("Authorization", jwtUtil.createToken(username));
    }

    public void logout(HttpServletResponse response) {

        response.setHeader("Authorization", jwtUtil.createToken(null));
    }

    @Transactional
    public void updateProfile(UpdateProfileDTO updateProfileDTO, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow();

        if (!passwordEncoder.matches(updateProfileDTO.getPassword(), user.getPassword())) {
        }
        
        String updateUsername = updateProfileDTO.getUsername();
        String updateEmail = updateProfileDTO.getEmail();
//        String introduce = updateProfileDTO.introduce();

        user.updateProfile(updateUsername, updateEmail);
    }

//    @Transactional
//    public void changePassword(ChangePasswordDTO changePasswordDTO, Long userId) {
//
//        String existingPassword = changePasswordDTO.existingPassword();
//        String newPassword = changePasswordDTO.newPassword();
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(StatusEnum.USER_NOT_FOUND));
//
//        validateChangingPassword(user, existingPassword, newPassword);
//
//        user.changePassword(passwordEncoder.encode(newPassword));
//        savePasswordHistory(userId, newPassword);
//    }
//
//    private void savePasswordHistory(Long userId, String password) {
//
//        PasswordHistory passwordHistory =
//                PasswordHistory.createPasswordHistory(password, userId);
//
//        passwordHistoryService.savePasswordHistory(passwordHistory);
//    }
//
//    private boolean isPasswordWithinLast3Times(Long userId, String newPassword) {
//
//        List<PasswordHistory> passwordHistoryList =
//                passwordHistoryService.findTop3PasswordHistory(userId);
//
//        for (PasswordHistory history : passwordHistoryList) {
//            if (passwordEncoder.matches(newPassword, history.getPassword())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void validateChangingPassword(User user, String existingPassword, String newPassword) {
//
//        if (!passwordEncoder.matches(existingPassword, user.getPassword())) {
//            throw new CustomException(StatusEnum.WRONG_PASSWORD_EXCEPTION);
//        }
//
//        if (!isPasswordWithinLast3Times(user.getUserId(), newPassword)) {
//            throw new CustomException(StatusEnum.DUPLICATE_PASSWORD_EXCEPTION);
//        }
//    }

}