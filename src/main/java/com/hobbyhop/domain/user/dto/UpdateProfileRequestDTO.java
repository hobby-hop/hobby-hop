package com.hobbyhop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileRequestDTO {
    private String info;

    @NotBlank(message = "oldPassword : 프로필 수정 시 필수 입력 값입니다.")
    private String oldPassword;

    @Size(min=8 , max=15)
    private String newPassword;

    private String confirmPassword;
}
