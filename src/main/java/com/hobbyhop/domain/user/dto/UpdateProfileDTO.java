package com.hobbyhop.domain.user.dto;

import lombok.Getter;

@Getter
public class UpdateProfileDTO {
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
//    private String password; //여기서 바꿀지 다른데서 비밀번호만 바꿀지 생각해보기
//    private String introduce; 이거 넣을지 말지 팀원들이랑 상의하기
}
