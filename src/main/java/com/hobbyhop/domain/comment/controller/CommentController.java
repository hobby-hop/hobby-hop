package com.hobbyhop.domain.comment.controller;

import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.global.request.SortStandardRequest;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/posts/{postId}/comments")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping
    public ApiResponse<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable Long clubId, @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ApiResponse.ok(commentService.postComment(request, clubId, postId, userDetails.getUser()));
    }

    @Operation(summary = "대댓글 작성")
    @PostMapping("/{commentId}")
    public ApiResponse<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ApiResponse.ok(commentService.postComment(request, clubId, postId, commentId, userDetails.getUser()));
    }

    @Operation(summary = "댓글 조회")
    @GetMapping
    public ApiResponse<?> getComments(Pageable pageable, @RequestBody SortStandardRequest standard, @PathVariable Long postId){
        // 호출시 ?page=보고 싶은 페이지&size=페이지에 들어갈 댓글 숫자 로 호출
        // ex) 2페이지에서 5개씩 보고 싶다. http://localhost:8080/api/groups/{groupId}/posts/{postId}/comments/sorted/1?page=2&size=5
        return ApiResponse.ok(commentService.getComments(pageable, standard, postId));
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ApiResponse<?> patchComment(@Valid @RequestBody CommentRequestDTO requestDto, @PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.patchComment(requestDto, clubId, postId, commentId, userDetails.getUser());
        return ApiResponse.ok("수정 성공");
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ApiResponse<?> deleteComment(@PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(clubId, postId, commentId, userDetails.getUser());
        return ApiResponse.ok("삭제 성공");
    }

    @Operation(summary = "댓글 좋아요")
    @PostMapping("/{commentId}/likes")
    public ApiResponse<?> likeComment(@PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.likeComment(clubId, postId, commentId, userDetails.getUser());
        return ApiResponse.ok("좋아요 변경 성공");
    }
}
