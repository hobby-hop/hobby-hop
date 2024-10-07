package com.hobbyhop.domain.postimage.controller;

import com.hobbyhop.domain.postimage.service.S3Service;
import com.hobbyhop.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PostImageController {
    private final S3Service s3Service;

    @Operation(summary = "게시글 이미지 업로드")
    @PostMapping
    public ApiResponse<?> imageUpload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.ok(s3Service.saveFile(file));
    }
    @Operation(summary = "게시글 이미지 삭제")
    @DeleteMapping("/{savedFileName}")
    public ApiResponse<?> imageDelete(@PathVariable("savedFileName") String savedFileName) {
        s3Service.deleteImage(savedFileName);

        return ApiResponse.ok("이미지 삭제 성공");
    }
}
