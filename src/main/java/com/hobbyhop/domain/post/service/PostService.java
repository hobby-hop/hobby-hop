package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface PostService {

    Post findById(Long postId);

    PostResponseDTO makePost(UserDetailsImpl userDetails, Long clubId, PostRequestDTO postRequestDTO);

    PostResponseDTO getPostById(Long clubId, Long postId);

    List<PostResponseDTO> getAllPost(Long clubId);

    PostResponseDTO modifyPost(UserDetailsImpl userDetails, Long clubId, Long postId, PostRequestDTO postRequestDTO);

    void deletePost(UserDetailsImpl userDetails, Long clubId, Long postId);
}
