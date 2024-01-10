package com.hobbyhop.domain.comment.controller;

import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.service.CommentService;
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
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> postComment(@RequestBody CommentRequestDTO request, @PathVariable Long postId){
        return ResponseEntity.ok(commentService.postComment(request, postId));
    }

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getComments(postId));
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
