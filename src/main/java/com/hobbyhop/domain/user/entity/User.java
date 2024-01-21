package com.hobbyhop.domain.user.entity;

import com.hobbyhop.domain.user.constant.UserRoleEnum;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
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

    @Column
    private Timestamp deletedAt;

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
