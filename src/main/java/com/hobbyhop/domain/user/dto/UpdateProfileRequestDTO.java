package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.user.dto.validCustom.NullablePattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {
    @NotBlank(message = "username: 이름을 입력해주세요.")
    @NullablePattern(regexp = "^[a-zA-Z0-9가-힣]+$",
                     msg1 = "username : 한글, 영어, 숫자만 입력해야 합니다.",
                     min = 1,
                     max = 50,
                     msg2 = "username : 이름은 최소 1자 이상, 최대 50자 이하로 입력해야 합니다.")
    private String username;

    @NotBlank(message = "email : 이메일을 입력해주세요.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$",
            message = "email : 이메일 형식이 올바르지 않습니다.")
    @Size(	min = 7,
            max = 50)
    private String email;

    @NotBlank(message = "info : 자기소개를 3~50자 입력해주세요.")
    @Size(	min = 3,
            max = 50)
    private String info;

    @NullablePattern(regexp = "^[a-zA-Z0-9]+$",
                     msg1 = "oldPassword : 비밀번호는 알파벳 대소문자, 숫자의 조합으로 입력해야 합니다.",
                     min = 8,
                     max = 15,
                     msg2 = "oldPassword : 비밀번호는 8자리 이상, 15자리 이하로 입력해야 합니다.")
    private String oldPassword;

    @NullablePattern(regexp = "^[a-zA-Z0-9]+$",
                     msg1 = "newPassword : 새 비밀번호는 알파벳 대소문자, 숫자의 조합으로 입력해야 합니다.",
                     min = 8,
                     max = 15,
                     msg2 = "newPassword : 새 비밀번호는 8자리 이상, 15자리 이하로 입력해야 합니다.")
    private String newPassword;

    @NullablePattern(regexp = "^[a-zA-Z0-9]+$",
                     msg1 = "confirmPassword : 새 비밀번호는 알파벳 대소문자, 숫자의 조합으로 입력해야 합니다.",
                     min = 8,
                     max = 15,
                     msg2 = "confirmPassword : 새 비밀번호는 8자리 이상, 15자리 이하로 입력해야 합니다.")
    private String confirmPassword;
}
