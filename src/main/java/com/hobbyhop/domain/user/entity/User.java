package com.hobbyhop.domain.user.entity;

import com.hobbyhop.domain.user.constant.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    public void updateProfile(String updateUsername, String updateEmail, String updatePassword) {
        if (updateUsername != null && !updateUsername.isEmpty()) {
            this.username = updateUsername;
        }
        if (updateEmail != null && !updateEmail.isEmpty()) {
            this.email = updateEmail;
        }
        if (updatePassword != null && !updatePassword.isEmpty()) {
            this.password = updatePassword;
        }
    }

    public void kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
    }
}
