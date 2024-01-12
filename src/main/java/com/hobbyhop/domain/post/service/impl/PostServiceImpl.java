package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.postlike.service.PostLikeService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.post.PostNotCorrespondUser;
import com.hobbyhop.global.exception.post.PostNotFoundException;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ClubService clubService;
    private final PostLikeService postLikeService;
    private final PostRepository postRepository;

    @Override
    public Post findPost(Long postId) {

        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public PostResponseDTO makePost(UserDetailsImpl userDetails, Long clubId, PostRequestDTO postRequestDTO) {

        Club club = clubService.findClub(clubId);

        Post post = Post.builder()
                .postTitle(postRequestDTO.getPostTitle())
                .postContent(postRequestDTO.getPostContent())
                .club(club)
                .user(userDetails.getUser())
                .likeCnt(0L)
                .build();

        postRepository.save(post);

        return PostResponseDTO.fromEntity(post);
    }

    @Override
    public PostResponseDTO getPostById(Long clubId, Long postId) {

        Post post = findAndCheckPostAndClub(clubId, postId);

        return PostResponseDTO.fromEntity(post);
    }

    public Post findAndCheckPostAndClub(Long clubId, Long postId){

        Club club = clubService.findClub(clubId);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(!club.getId().equals(post.getClub().getId())){
            throw new PostNotCorrespondUser();
        }

        return post;
    }

    @Override
    public PostPageResponseDTO getAllPost(Pageable pageable, Long clubId) {

        return postRepository.findAllByClubId(pageable, clubId);
    }

    @Override
    @Transactional
    public PostResponseDTO modifyPost(UserDetailsImpl userDetails, Long clubId, Long postId, PostRequestDTO postRequestDTO){

        Post post = findAndCheckPostAndClub(clubId, postId);

        if(!userDetails.getUser().getId().equals(post.getUser().getId())){
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

        Post post = findAndCheckPostAndClub(clubId, postId);

        if(!userDetails.getUser().getId().equals(post.getUser().getId())){
            throw new PostNotCorrespondUser();
        }

        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void makePostLike(UserDetailsImpl userDetails, Long clubId, Long postId){

        User user = userDetails.getUser();
        Post post = findAndCheckPostAndClub(clubId, postId);

        postLikeService.postLike(user, post);
    }
}
