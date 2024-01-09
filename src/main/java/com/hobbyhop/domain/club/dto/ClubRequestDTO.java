package com.hobbyhop.domain.club.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubRequestDTO {
    private String title;
    private String content;
    private Long categoryId;
}
