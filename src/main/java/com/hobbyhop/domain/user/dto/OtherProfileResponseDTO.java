package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OtherProfileResponseDTO {
    private String username;
    private String info;

    public static OtherProfileResponseDTO fromEntity (User user) {
        return OtherProfileResponseDTO.builder()
                .username(user.getUsername())
                .info(user.getInfo())
                .build();
    }
}
