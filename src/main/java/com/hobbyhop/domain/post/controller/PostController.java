package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs/{clubId}")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse> makePost(@PathVariable Long clubId,
            @RequestBody @Valid PostRequestDTO postRequestDTO) {
        //@AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.ok(ApiResponse.ok(
                postService.makePost(clubId, postRequestDTO)
        ));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> getPostById(@PathVariable Long clubId,
            @PathVariable Long postId) {

        return ResponseEntity.ok(ApiResponse.ok(
                postService.getPostById(clubId, postId)
        ));

    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse> getAllPost(@PathVariable Long clubId) {

        return ResponseEntity.ok(ApiResponse.ok(
                postService.getAllPost(clubId)
        ));
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> modifyPost(@PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestBody @Valid PostRequestDTO postRequestDTO) {

        return ResponseEntity.ok(ApiResponse.ok(
                postService.modifyPost(clubId, postId, postRequestDTO)
        ));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long clubId,
            @PathVariable Long postId) {

        postService.deletePost(clubId, postId);
        return ResponseEntity.ok(ApiResponse.ok(
                "삭제 성공"
        ));
    }
}
