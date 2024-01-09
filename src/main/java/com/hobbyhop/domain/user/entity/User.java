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
	private UserRoleEnum role = UserRoleEnum.USER;

	@Builder
	public User(String username, String email, String password, UserRoleEnum role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public void updateProfile(String updateUsername, String updateEmail) {
		this.username = updateUsername;
		this.email = updateEmail;
	}
}
