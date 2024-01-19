package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.s3.S3Service;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/clubs/{clubId}/posts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

    @PostMapping
    public ApiResponse<?> makePost(@PathVariable Long clubId,
            @RequestBody @Valid PostRequestDTO postRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ApiResponse.ok(postService.makePost(userDetails.getUser(), clubId, postRequestDTO));
    }

    @PostMapping("/{postId}")
    public ApiResponse<?> imageUploadPost(@PathVariable Long clubId, @PathVariable Long postId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        postService.imageUploadPost(userDetails.getUser(), clubId, postId, file);
        return ApiResponse.ok("이미지 업로드 성공");
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
            @RequestBody @Valid PostRequestDTO postRequestDTO, @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return ApiResponse.ok(postService.modifyPost(userDetails.getUser(), clubId, postId, file,postRequestDTO));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable Long clubId, @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(userDetails.getUser(), clubId, postId);
        return ApiResponse.ok("삭제 성공");
    }

    @PostMapping("/{postId}/likes")
    public ApiResponse<?> likePost(@PathVariable Long clubId,
            @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.makePostUser(userDetails.getUser(), clubId, postId);
        return ApiResponse.ok("좋아요 성공");
    }

    @PostMapping(value = "/imageUpload")
    public ApiResponse<?> imageUpload(@RequestParam("file") MultipartFile file) throws IOException {

        String originFilename = s3Service.saveFile(file);
        return ApiResponse.ok("좋아요 성공");
    }
}
