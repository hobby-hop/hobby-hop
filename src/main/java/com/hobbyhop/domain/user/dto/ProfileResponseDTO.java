package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDTO {
    private String username;
    private String email;
    private String info;

    public static ProfileResponseDTO fromEntity (User user) {
        return ProfileResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .info(user.getInfo())
                .build();
    }
}