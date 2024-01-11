package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.ClubRepository;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.global.exception.club.ClubNotFoundException;
import com.hobbyhop.global.exception.post.PostNotCorrespondUser;
import com.hobbyhop.global.exception.post.PostNotFoundException;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ClubRepository clubRepository;

    @Override
    public Post findById(Long postId) {

        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public PostResponseDTO makePost(UserDetailsImpl userDetails, Long clubId, PostRequestDTO postRequestDTO) {

        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        Post post = Post.builder()
                .postTitle(postRequestDTO.getPostTitle())
                .postContent(postRequestDTO.getPostContent())
                .club(club)
                .user(userDetails.getUser())
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
    public PostResponseDTO modifyPost(UserDetailsImpl userDetails, Long clubId, Long postId, PostRequestDTO postRequestDTO){

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        User dbuser = userRepository.findById(userDetails.getUser().getId()).orElseThrow();

        if(!dbuser.getId().equals(post.getUser().getId())){
            throw new PostNotCorrespondUser();
        }

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
    public void deletePost(UserDetailsImpl userDetails, Long clubId, Long postId){

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        User dbuser = userRepository.findById(userDetails.getUser().getId()).orElseThrow();

        if(!dbuser.getId().equals(post.getUser().getId())){
            throw new PostNotCorrespondUser();
        }

        postRepository.deleteById(postId);
    }
}
