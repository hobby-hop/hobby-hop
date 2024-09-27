package com.hobbyhop.domain.postimage.controller;

import com.hobbyhop.domain.postimage.dto.PostImageDTO;
import com.hobbyhop.domain.postimage.service.S3Service;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Log4j2
public class PostImageController {
    private final S3Service s3Service;

    @Operation(summary = "게시글 이미지 업로드")
    @PostMapping
    public ApiResponse<?> imageUpload(@RequestParam("file") MultipartFile file,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        PostImageDTO postImageDTO = s3Service.saveFile(file);
        log.info(postImageDTO);
        return ApiResponse.ok(postImageDTO);
    }
    @Operation(summary = "게시글 이미지 삭제")
    @DeleteMapping("/{savedFileName}")
    public ApiResponse<?> imageDelete(@PathVariable("savedFileName") String savedFileName) {
        s3Service.deleteImage(savedFileName);
        return ApiResponse.ok("이미지 삭제 성공");
    }
}
