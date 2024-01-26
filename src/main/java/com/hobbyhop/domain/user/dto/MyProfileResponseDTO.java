package com.hobbyhop.domain.user.dto;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyProfileResponseDTO {
    private String username;
    private String email;
    private String password;
//    private String introduce;
    private List<Club> clubList;
    private List<Post> postList;
}
