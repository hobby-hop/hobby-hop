package com.hobbyhop.domain.category.dto;

import com.hobbyhop.domain.club.dto.ClubElementVO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryElementVO {
    Long clubId;
    Long postId;
    Long commentId;
}
