package com.hobbyhop.domain.comment.controller;

import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/groups/{groupId}/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(commentService.postComment(request, postId, userDetails.getUser()));
    }

    @GetMapping
    public ResponseEntity<?> getComments(Pageable pageable, @PathVariable Long postId){
        // 호출시 ?page=보고 싶은 페이지&size=페이지에 들어갈 댓글 숫자 로 호출
        // ex) 2페이지에서 5개씩 보고 싶다. http://localhost:8080/api/groups/{groupId}/posts/{postId}/comments?page=2&size=5
        return ResponseEntity.ok(commentService.getComments(pageable, postId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> patchComment(@Valid @RequestBody CommentRequestDTO requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.patchComment(requestDto, commentId, userDetails.getUser());
        return ResponseEntity.ok().body("수정 성공");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok().body("삭제 성공");
    }

    @PostMapping("/{commentId}/likes")
    public ResponseEntity<?> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.likeComment(commentId, userDetails.getUser());
        return ResponseEntity.ok().body("좋아요 변경 성공");
    }
}
