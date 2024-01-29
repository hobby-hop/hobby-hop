package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyProfileResponseDTO {
    private String username;
    private String email;

    public static MyProfileResponseDTO fromEntity (User user) {
        return MyProfileResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
