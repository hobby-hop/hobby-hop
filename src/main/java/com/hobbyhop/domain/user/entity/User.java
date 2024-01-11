package com.hobbyhop.domain.user.entity;

import com.hobbyhop.domain.user.constant.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.hobbyhop.domain.user.constant.UserRoleEnum.USER;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
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

//	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role = USER;

	private Long kakaoId;

	@Builder
	public User(String username, String email, String password, UserRoleEnum role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = USER;
	}

	public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.kakaoId =kakaoId;
	}

//	public void updateProfile(String updateUsername, String updateEmail, String updatePassword) {
//		this.username = updateUsername;
//		this.email = updateEmail;
//		this.password = updatePassword;
//	}

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

	public User kakaoIdUpdate(Long kakaoId) {
		this.kakaoId = kakaoId;
		return this;
	}

}
/*
package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId =kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}
 */