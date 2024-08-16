package com.hobbyhop.domain.club.dto;

import com.hobbyhop.domain.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public static ClubResponseDTO fromEntity(Club club) {
        return ClubResponseDTO.builder()
                .id(club.getId())
                .title(club.getTitle())
                .content(club.getContent())
                .categoryId(club.getCategory().getId())
                .categoryName(club.getCategory().getCategoryName())
                .createdAt(club.getCreatedAt())
                .modifiedAt(club.getModifiedAt())
                .build();
    }
}
