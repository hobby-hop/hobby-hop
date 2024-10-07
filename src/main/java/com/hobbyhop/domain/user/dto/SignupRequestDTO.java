package com.hobbyhop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {
    @NotBlank(message = "username : 이름을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$",
            message = "username : 한글, 영어, 숫자만 입력해야 합니다.")
    @Size(min = 1,
            max = 12,
            message = "username : 이름은 최소 1자 이상, 최대 12자 이하로 입력해야 합니다.")
    private String username;

    @NotBlank(message = "email : 이메일을 입력해주세요.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$",
            message = "email : 이메일 형식이 올바르지 않습니다.")
    @Size(min = 7,
            max = 50)
    private String email;

    @Size(max = 50,
            message = "info : 자기소개를 50자 미만으로 입력해주세요.")
    private String info;

    @NotBlank(message = "password : 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "password : 비밀번호는 알파벳 대소문자, 숫자의 조합으로 입력해야 합니다.")
    @Size(min = 8,
            max = 15,
            message = "password : 비밀번호는 8자리 이상, 15자리 이하로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "confirmPassword : 비밀번호를 다시 입력해주세요.")
    private String confirmPassword;
}