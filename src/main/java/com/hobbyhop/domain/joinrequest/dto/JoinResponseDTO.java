package com.hobbyhop.domain.joinrequest.dto;

import com.hobbyhop.domain.joinrequest.entity.JoinRequest;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinResponseDTO {
    private Long id;
    private Long sendUserId;
    private Long recvClubId;
    private String username;

    public static JoinResponseDTO fromEntity(JoinRequest joinRequest) {
        return JoinResponseDTO.builder()
                .id(joinRequest.getId())
                .sendUserId(joinRequest.getUser().getId())
                .recvClubId(joinRequest.getClub().getId())
                .username(joinRequest.getUser().getUsername())
                .build();
    }
}
