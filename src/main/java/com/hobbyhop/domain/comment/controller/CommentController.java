package com.hobbyhop.domain.comment.controller;

import com.hobbyhop.domain.comment.dto.CommentPageRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/posts/{postId}/comments")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping
    public ApiResponse<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ApiResponse.ok(commentService.postComment(request, clubId, postId, userDetails.getUser()));
    }

    @Operation(summary = "대댓글 작성")
    @PostMapping("/{commentId}")
    public ApiResponse<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ApiResponse.ok(commentService.postComment(request, clubId, postId, commentId, userDetails.getUser()));
    }

    @Operation(summary = "댓글 조회")
    @GetMapping
    public ApiResponse<?> getComments(CommentPageRequestDTO pageRequestDTO, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        return ApiResponse.ok(commentService.getComments(pageRequestDTO, postId, null));
    }

    @Operation(summary = "대댓글 조회")
    @GetMapping("/{commentId}")
    public ApiResponse<?> getComments(CommentPageRequestDTO pageRequestDTO, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId){
        return ApiResponse.ok(commentService.getComments(pageRequestDTO, postId, commentId));
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ApiResponse<?> patchComment(@Valid @RequestBody CommentRequestDTO requestDto, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.patchComment(requestDto, clubId, postId, commentId, userDetails.getUser());
        return ApiResponse.ok("수정 성공");
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ApiResponse<?> deleteComment(@PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(clubId, postId, commentId, userDetails.getUser());
        return ApiResponse.ok("삭제 성공");
    }

    @Operation(summary = "댓글 좋아요")
    @PostMapping("/{commentId}/likes")
    public ApiResponse<?> likeComment(@PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.likeComment(clubId, postId, commentId, userDetails.getUser());
        return ApiResponse.ok("좋아요 변경 성공");
    }
}
