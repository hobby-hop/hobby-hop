package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.*;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.response.PageResponseDTO;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;


public interface PostService {
    Post findPost(Long postId, Long clubId);

    PostResponseDTO writePost(PostRequestDTO postRequestDTO, Long clubId, User user);

    PostDetailResponseDTO getPostById(Long clubId, Long postId, User user);

    PageResponseDTO<PostPageResponseDTO> getAllPost(PostPageRequestDTO pageRequestDTO, Long clubId);

    PostResponseDTO modifyPost(User user, Long clubId, Long postId, PostModifyRequestDTO postModifyRequestDTO)
            throws IOException;

    void deletePost(User user, Long clubId, Long postId);

    Long likePost(User user, Long clubId, Long postId);
}
