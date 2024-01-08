package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post findById(Long postId) {

        return postRepository.findById(postId).orElseThrow();
    }

    @Override
    public PostResponseDTO createPost(Long clubId, PostRequestDTO postRequestDTO) {

        Post post = Post.builder()
                .postTitle(postRequestDTO.getPostTitle())
                .postContent(postRequestDTO.getPostContent())
                .build();

        Post savedPost = postRepository.save(post);

        return new PostResponseDTO(savedPost);
    }

    @Override
    public PostResponseDTO getPostById(Long clubId, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow();

        return new PostResponseDTO().getDto(post);
    }
}
