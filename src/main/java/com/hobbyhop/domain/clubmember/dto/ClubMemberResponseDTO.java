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
    private Long userId;
    private Long clubId;

    public static ClubMemberResponseDTO fromEntity(ClubMember clubMember) {
        return ClubMemberResponseDTO.builder()
                .userId(clubMember.getClubMemberPK().getUser().getId())
                .clubId(clubMember.getClubMemberPK().getClub().getId()).build();
    }
}