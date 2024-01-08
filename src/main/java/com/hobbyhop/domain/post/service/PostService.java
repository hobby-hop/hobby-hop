package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface PostService {

    Post findById(Long postId);

    PostResponseDTO makePost(Long clubId, PostRequestDTO postRequestDTO);

    PostResponseDTO getPostById(Long clubId, Long postId);

    List<PostResponseDTO> getAllPost(Long clubId);

    PostResponseDTO modifyPost(Long clubId, Long postId, PostRequestDTO postRequestDTO);

    void deletePost(Long clubId, Long postId);
}
