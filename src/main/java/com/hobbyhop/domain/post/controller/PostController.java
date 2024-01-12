package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clubs/{clubId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> makePost(@PathVariable Long clubId,
            @RequestBody @Valid PostRequestDTO postRequestDTO,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.ok(ApiResponse.ok(
                postService.makePost(userDetails, clubId, postRequestDTO)
        ));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPostById(@PathVariable Long clubId,
            @PathVariable Long postId) {

        return ResponseEntity.ok(ApiResponse.ok(
                postService.getPostById(clubId, postId)
        ));

    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPost(@PathVariable Long clubId) {

        return ResponseEntity.ok(ApiResponse.ok(
                postService.getAllPost(clubId)
        ));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse> modifyPost(@PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestBody @Valid PostRequestDTO postRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(ApiResponse.ok(
                postService.modifyPost(userDetails, clubId, postId, postRequestDTO)
        ));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long clubId,
            @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(userDetails, clubId, postId);
        return ResponseEntity.ok(ApiResponse.ok(
                "삭제 성공"
        ));
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Long clubId,
            @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.makePostLike(userDetails, clubId, postId);
        return ResponseEntity.ok(ApiResponse.ok(
                "좋아요 성공"
        ));
    }
}
