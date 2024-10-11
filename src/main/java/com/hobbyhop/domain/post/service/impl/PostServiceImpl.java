package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.post.dto.*;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.postimage.service.S3Service;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.postuser.service.PostUserService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.post.PostNotCorrespondUser;
import com.hobbyhop.global.exception.post.PostNotFoundException;
import com.hobbyhop.global.response.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostUserService postUserService;
    private final S3Service s3Service;
    private final ClubMemberService clubMemberService;
    private final ClubService clubService;

    @Override
    public Post findPost(Long clubId, Long postId) {
        return postRepository.findByIdAndClub_Id(postId, clubId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public PostResponseDTO writePost(PostRequestDTO postRequestDTO, Long clubId, User user) {
        if (!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Club club = clubService.findClub(clubId);

        Post post = Post.buildPost(postRequestDTO, club, user);
        postRepository.save(post);

        return PostResponseDTO.fromEntity(post);
    }

    @Override
    public PostDetailResponseDTO getPostById(Long clubId, Long postId, User user) {
        if (!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Post post = postRepository.findByIdWithImages(clubId, postId).orElseThrow(PostNotFoundException::new);
        PostUser postUser = postUserService.findPostUser(user, post);
        boolean isLiked = postUser != null;

        return PostDetailResponseDTO.fromEntity(post, isLiked);
    }

    @Override
    public PageResponseDTO<PostPageResponseDTO> getAllPost(PostPageRequestDTO pageRequestDTO, Long clubId) {
        Page<PostPageResponseDTO> result = postRepository.findAllByClubId(pageRequestDTO, clubId);

        return PageResponseDTO.<PostPageResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total(result.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public PostResponseDTO modifyPost(User user, Long clubId, Long postId, PostModifyRequestDTO postModifyRequestDTO) {
        if (!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Post post = findPost(clubId, postId);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new PostNotCorrespondUser();
        }

        post.clearImages();
        applyChanges(post, postModifyRequestDTO);

        return PostResponseDTO.fromEntity(post);
    }

    @Override
    @Transactional
    public void deletePost(User user, Long clubId, Long postId) {
        if (!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Post post = findPost(clubId, postId);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new PostNotCorrespondUser();
        }

        List<String> savedFileNames = post.getImageSet().stream()
                .map(image -> image.getUuid() + image.getFileName())
                .collect(Collectors.toList());

        postRepository.deleteAllElement(postId);
        removeFiles(savedFileNames);
    }

    @Override
    @Transactional
    public Long likePost(User user, Long clubId, Long postId) {
        if(!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Post post = postRepository.findByIdWithOptimisticLock(postId)
                .orElseThrow(PostNotFoundException::new);

        return postUserService.togglePostUser(user, post);
    }

    private void applyChanges(Post post, PostModifyRequestDTO postModifyRequestDTO) {
        if (postModifyRequestDTO.getTitle() != null) {
            post.changeTitle(postModifyRequestDTO.getTitle());
        }
        if (postModifyRequestDTO.getContent() != null) {
            post.changeContent(postModifyRequestDTO.getContent());
        }
        if (postModifyRequestDTO.getPostImages() != null) {
            postModifyRequestDTO.getPostImages().forEach(postImage -> {
                String[] arr = postImage.getSavedFileName().split("_");
                post.addImage(arr[0], arr[1], postImage.getSavedFileUrl());
            });
        }
    }

    public void removeFiles(List<String> files) {
        for (String savedFileName : files) {
            s3Service.deleteImage(savedFileName);
        }
    }
}
