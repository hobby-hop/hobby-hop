package com.hobbyhop.domain.club.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubElementVO {
    Long postId;
    List<Long> commentId;
}
