package com.hobbyhop.domain.clubmember.dto;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMemberResponseDTO {
    private Long id;
    private Long userId;
    private Long clubId;

    public static ClubMemberResponseDTO fromEntity(ClubMember clubMember) {
        return ClubMemberResponseDTO.builder()
                .id(clubMember.getId())
                .userId(clubMember.getUser().getId())
                .clubId(clubMember.getClub().getId())
                .build();

    }
}
