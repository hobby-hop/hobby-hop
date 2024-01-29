package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.post.dto.PostModifyRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.s3.S3Service;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.postuser.service.PostUserService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.common.UnAuthorizedModifyException;
import com.hobbyhop.global.exception.post.PostNotCorrespondUser;
import com.hobbyhop.global.exception.post.PostNotFoundException;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.PageResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ClubService clubService;
    private final PostUserService postUserService;
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final ClubMemberService clubMemberService;

    @Override
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    @Transactional
    public PostResponseDTO makePost(User user, Long clubId, PostRequestDTO postRequestDTO) {

        if(!clubMemberService.isClubMember(clubId, user.getId()))
            throw new ClubMemberNotFoundException();

        Club club = clubService.findClub(clubId);

        clubMemberService.findByClubAndUser(clubId, user.getId());

        Post post = Post.builder()
                .postTitle(postRequestDTO.getPostTitle())
                .postContent(postRequestDTO.getPostContent())
                .club(club)
                .user(user)
                .likeCnt(0L)
                .build();

        postRepository.save(post);

        return PostResponseDTO.fromEntity(post);
    }

    @Override
    @Transactional
    public void imageUploadPost(User user, Long clubId, Long postId, MultipartFile file) {
        Post post = checkAuth(clubId, postId, user.getId());

        String originFilename = s3Service.saveFile(file);
        String savedFilename = UUID.randomUUID() + "_" + originFilename;

        post.modifyPost(null, null, originFilename, savedFilename);
    }

    @Override
    public PostResponseDTO getPostById(Long clubId, Long postId) {
        Post post = findAndCheckPostAndClub(clubId, postId);

        return PostResponseDTO.fromEntity(post);
    }

    @Override
    public PageResponseDTO<PostPageResponseDTO> getAllPost(PageRequestDTO pageRequestDTO, Long clubId) {
        Page<PostPageResponseDTO> result = postRepository.findAllByClubId(pageRequestDTO.getPageable(), clubId, pageRequestDTO.getKeyword());

        return PageResponseDTO.<PostPageResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total(Long.valueOf(result.getTotalElements()).intValue())
                .build();
    }

    @Override
    @Transactional
    public PostResponseDTO modifyPost(User user, Long clubId, Long postId, MultipartFile file, PostModifyRequestDTO postModifyRequestDTO) {
        Post post = checkAuth(clubId, postId, user.getId());

        String originFilename = null;
        String savedFilename = null;


        if(file != null && !file.isEmpty()){
            originFilename = s3Service.saveFile(file);
            savedFilename = UUID.randomUUID() + "_" + originFilename;
        }

        post.modifyPost(postModifyRequestDTO.getPostTitle(), postModifyRequestDTO.getPostContent(), originFilename, savedFilename);

        return PostResponseDTO.fromEntity(post);
    }

    @Override
    @Transactional
    public void deletePost(User user, Long clubId, Long postId){
        checkAuth(clubId, postId, user.getId());

        postRepository.deleteAllElement(postId);
    }

    @Override
    @Transactional
    public void makePostUser(User user, Long clubId, Long postId){
        Post post = findAndCheckPostAndClub(clubId, postId);

        postUserService.postUser(user, post);
    }

    private Post findAndCheckPostAndClub(Long clubId, Long postId){
        Club club = clubService.findClub(clubId);

        Post post = findPost(postId);

        if(!club.getId().equals(post.getClub().getId())){
            throw new PostNotCorrespondUser();
        }

        return post;
    }

    private Post checkAuth(Long clubId, Long postId, Long userId){
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, userId);

        if(!clubMember.getMemberRole().equals(MemberRole.ADMIN))
            throw new UnAuthorizedModifyException();

        if(!clubMemberService.isClubMember(clubId, userId))
            throw new ClubMemberNotFoundException();

        Post post = findAndCheckPostAndClub(clubId, postId);

        if(!post.getUser().getId().equals(userId))
            throw new PostNotCorrespondUser();

        return post;
    }
}