package com.hobbyhop.domain.user.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id=?")  // 이걸 삭제하든지
@Where(clause = "deleted_at is NULL")                               // 이걸 삭제하든지 해야 회원 탈퇴 시 DB 에서 지워지고 같은 회원정보로 회원가입해도 중복 에러가 안 떠요!
public class User extends BaseEntity {
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

    public void updateProfile (String updateUsername, String updateEmail, String updatePassword) {
        if (!updateUsername.isBlank()) {
            this.username = updateUsername;
        }
        if (!updateEmail.isBlank()) {
            this.email = updateEmail;
        }
        if (!updatePassword.isBlank()) {
            this.password = updatePassword;
        }
    }

    public void kakaoIdUpdate (Long kakaoId) {
        this.kakaoId = kakaoId;
    }
}
