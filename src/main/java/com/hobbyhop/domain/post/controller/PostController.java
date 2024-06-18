package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostModifyRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageRequestDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.s3.S3Service;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/clubs/{clubId}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final S3Service s3Service;

    @Operation(summary = "게시글 작성")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ApiResponse<?> makePost(@PathVariable(name = "clubId") Long clubId,
                                   @RequestBody @Valid PostRequestDTO postRequestDTO,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ApiResponse.ok(postService.makePost(userDetails.getUser(), clubId, postRequestDTO));
    }

    @Operation(summary = "게시글 이미지 업로드")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{postId}")
    public ApiResponse<?> imageUploadPost(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId,
                                          @RequestParam("file") MultipartFile file,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        postService.imageUploadPost(userDetails.getUser(), clubId, postId, file);

        return ApiResponse.ok("이미지 업로드 성공");
    }

    @Operation(summary = "게시글 단일 조회")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{postId}")
    public ApiResponse<?> getPostById(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(postService.getPostById(userDetails.getUser(), clubId, postId));
    }

    @Operation(summary = "게시글 전체 조회")
    @GetMapping
    public ApiResponse<?> getAllPost(PostPageRequestDTO pageRequestDTO, @PathVariable(name = "clubId") Long clubId) {
        return ApiResponse.ok(postService.getAllPost(pageRequestDTO, clubId));
    }

    @Operation(summary = "게시글 수정")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{postId}")
    public ApiResponse<?> modifyPost(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId,
                                     @RequestBody @Valid PostModifyRequestDTO postModifyRequestDTO, @RequestParam(required = false, value = "file") MultipartFile file,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ApiResponse.ok(postService.modifyPost(userDetails.getUser(), clubId, postId, file, postModifyRequestDTO));
    }

    @Operation(summary = "게시글 삭제")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "postId") Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(userDetails.getUser(), clubId, postId);

        return ApiResponse.ok("삭제 성공");
    }

    @Operation(summary = "게시글 좋아요")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{postId}/likes")
    public ApiResponse<?> likePost(@PathVariable(name = "clubId") Long clubId,
                                   @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.makePostUser(userDetails.getUser(), clubId, postId);

        return ApiResponse.ok("좋아요 성공");
    }
}
