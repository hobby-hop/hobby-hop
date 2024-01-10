package com.hobbyhop.domain.user.dto;

import lombok.Getter;

@Getter
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
