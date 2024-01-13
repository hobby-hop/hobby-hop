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

//	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role = USER;

	private Long kakaoId;

	@Builder
	public User(String username, String email, String password) {
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
