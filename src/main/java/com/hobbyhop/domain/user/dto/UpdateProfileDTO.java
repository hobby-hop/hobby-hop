package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.user.dto.validCustom.NullablePattern;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateProfileDTO {
    @NullablePattern(regexp = "^[a-zA-Z0-9가-힣]+$",
                     msg1 = "username : 한글, 영어, 숫자만 입력해야 합니다.",
                     min = 1,
                     max = 50,
                     msg2 = "username : 이름은 최소 1자 이상, 최대 50자 이하로 입력해야 합니다.")
    private String username;

    @Email
    private String email;

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

//    private String introduce; 이거 넣을지 말지 팀원들이랑 상의하기
}
