package com.hobbyhop.domain.cocomment;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping
    public ResponseEntity<Response<CommentResponse>> createComment(@PathVariable Long userId,
                                                                   @RequestBody CommentRequest request,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Long userId = user.getId();
        CommentResponse createdComment = commentService.createComment(userId, request, userId);
        return ResponseEntity.ok(Response.success(createdComment));
    }

    @Operation(summary = "댓글 업데이트")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Response<CommentResponse>> updateComment(@PathVariable Long commentId,
                                                                   @RequestBody CommentRequest request,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        CommentResponse updatedComment = commentService.updateComment(commentId, request, user.getId());
        return ResponseEntity.ok(Response.success(updatedComment));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response<Void>> deleteComment(@PathVariable Long commentId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok(Response.success());
    }
}
