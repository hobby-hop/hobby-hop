package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.global.exception.post.PostNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post findById(Long postId) {

        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public PostResponseDTO makePost(Long clubId, PostRequestDTO postRequestDTO) {

        Post post = Post.builder()
                .postTitle(postRequestDTO.getPostTitle())
                .postContent(postRequestDTO.getPostContent())
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(savedPost);
    }

    @Override
    public PostResponseDTO getPostById(Long clubId, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        return PostResponseDTO.getDto(post);
    }

    @Override
    public List<PostResponseDTO> getAllPost(Long postId) {

        List<PostResponseDTO> list = postRepository.findAll().stream().map(post ->
                PostResponseDTO.builder()
                        .postId(post.getId())
                        .postTitle(post.getPostTitle())
                        .postContent(post.getPostContent())
                        .createAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        ).collect(Collectors.toList());

        return list;
    }

    @Override
    @Transactional
    public PostResponseDTO modifyPost(Long clubId, Long postId, PostRequestDTO postRequestDTO){

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(postRequestDTO.getPostTitle() != null) {
            post.changeTitle(postRequestDTO.getPostTitle());
        }

        if(postRequestDTO.getPostContent() != null) {
            post.changeContent(postRequestDTO.getPostContent());
        }

        if(postRequestDTO.getImageUrl() != null) {
            post.changeImageUrl(postRequestDTO.getImageUrl());
        }

        Post modifiedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(modifiedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long clubId, Long postId){

        postRepository.deleteById(postId);
    }
}
