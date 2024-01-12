package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs/{clubId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<?> makePost(@PathVariable Long clubId,
            @RequestBody @Valid PostRequestDTO postRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ApiResponse.ok(postService.makePost(userDetails, clubId, postRequestDTO));
    }

    @GetMapping("/{postId}")
    public ApiResponse<?> getPostById(@PathVariable Long clubId, @PathVariable Long postId) {

        return ApiResponse.ok(postService.getPostById(clubId, postId));

    }

    @GetMapping
    public ApiResponse<?> getAllPost(final Pageable pageable, @PathVariable Long clubId) {

        return ApiResponse.ok(postService.getAllPost(pageable, clubId));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<?> modifyPost(@PathVariable Long clubId, @PathVariable Long postId,
            @RequestBody @Valid PostRequestDTO postRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ApiResponse.ok(postService.modifyPost(userDetails, clubId, postId, postRequestDTO));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable Long clubId, @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(userDetails, clubId, postId);
        return ApiResponse.ok("삭제 성공");
    }

    @PostMapping("/{postId}/likes")
    public ApiResponse<?> likePost(@PathVariable Long clubId,
            @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.makePostLike(userDetails, clubId, postId);
        return ApiResponse.ok("좋아요 성공");
    }
}
