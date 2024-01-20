package com.hobbyhop.domain.club.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubRequestDTO {
    @NotBlank(message = "모임 명을 입력해주세요")
    @Size(min = 1, max = 25)
    private String title;
    @NotBlank(message = "모임 소개를 입력해주세요")
    private String content;
    @NotNull(message = "모임의 카테고리를 선택해주세요")
    private Long categoryId;
}
