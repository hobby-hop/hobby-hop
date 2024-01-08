package com.hobbyhop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

	@Email
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp = "[a-zA-Z0-9]+$", message = "비밀번호는 알파벳 대소문자, 숫자의 조합으로 입력해야 합니다.")
	@Size(min = 8, max = 15, message = "비밀번호는 8자리 이상, 15자리 이하로 입력해야 합니다.")
	private String password;
}
