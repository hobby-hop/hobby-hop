package com.hobbyhop.domain.user.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 50)
    private String info;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private Timestamp deletedAt;

    private Long kakaoId;

    public void updateProfile (String updateInfo, String updatePassword) {
            this.info = updateInfo;
            this.password = updatePassword;
    }

    public void changeInfo(String updateInfo) {
        this.info = updateInfo;
    }

    public void changePassword(String updatePassword) {
        this.password = updatePassword;
    }

    public void kakaoIdUpdate (Long kakaoId) {
        this.kakaoId = kakaoId;
    }
}
