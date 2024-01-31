package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.user.dto.validCustom.NullablePattern;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {
    private String info;

    @NotBlank(message = "oldPassword : 프로필 수정 시 필수 입력 값입니다.")
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
