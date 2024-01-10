package com.hobbyhop.domain.comment.controller;

import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable Long postId){
        return ResponseEntity.ok(commentService.postComment(request, postId));
    }

    @GetMapping
    public ResponseEntity<?> getComments(Pageable pageable, @PathVariable Long postId){
        // 호출시 ?page=보고 싶은 페이지&size=페이지에 들어갈 댓글 숫자 로 호출
        // ex) 2페이지에서 5개씩 보고 싶다. http://localhost:8080/api/groups/{groupId}/posts/{postId}/comments?page=2&size=5
        return ResponseEntity.ok(commentService.getComments(pageable, postId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> patchComment(@Valid @RequestBody CommentRequestDTO requestDto, @PathVariable Long commentId){
        commentService.patchComment(requestDto, commentId);
        return ResponseEntity.ok().body("수정 성공");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body("삭제 성공");
    }
}
