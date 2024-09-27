package com.hobbyhop.domain.comment.service.impl;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.CommentRepository;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.auth.UnAuthorizedModifyException;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.comment.CommentNotFoundException;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentUserService commentUserService;
    private final PostService postService;
    private final ClubMemberService clubMemberService;

    @Override
    public List<CommentResponseDTO> getComments(Long clubId, Long postId, User user) {
        if(!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        return commentRepository.findAllByPostId(postId, user.getId());
    }

    @Override
    public CommentResponseDTO writeComment(CommentRequestDTO request, Long clubId, Long postId, User user) {
        if (!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Post post = postService.findPost(postId, clubId);
        Comment comment = Comment.buildComment(request, post, user, null);
        commentRepository.save(comment);

        return CommentResponseDTO.fromEntity(comment);
    }

    @Override
    public CommentResponseDTO writeReply(CommentRequestDTO request, Long clubId, Long postId, Long commentId, User user) {
        if (!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Post post = postService.findPost(postId, clubId);
        Comment comment = findById(clubId, postId, commentId);
        Comment reply = Comment.buildComment(request, post, user, comment);
        commentRepository.save(reply);
        comment.getReplies().add(reply);

        return CommentResponseDTO.fromEntity(reply);
    }

    @Override
    @Transactional
    public CommentResponseDTO editComment(CommentRequestDTO requestDto, Long clubId, Long postId, Long commentId, User user) {
        Comment comment = checkAuth(clubId, postId, commentId, user);
        comment.changeContent(requestDto.getContent());

        return CommentResponseDTO.fromEntity(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long clubId, Long postId, Long commentId, User user) {
        Comment comment = checkAuth(clubId, postId, commentId, user);
        Map<Long, Comment> deleteList = makeDeleteList(comment);

        commentRepository.deleteList(deleteList.values().stream().toList());
    }

    @Override
    @Transactional
    public Long likeComment(Long clubId, Long postId, Long commentId, User user) {
        if(!clubMemberService.isClubMember(clubId, user.getId())) {
            throw new ClubMemberNotFoundException();
        }

        Comment comment = commentRepository.findByIdWithOptimisticLock(commentId)
                .orElseThrow(CommentNotFoundException::new);

        return commentUserService.toggleCommentUser(comment, user);
    }

    @Override
    public Comment findById(Long clubId, Long postId, Long commentId) {
        return commentRepository.findById(clubId, postId, commentId).orElseThrow(CommentNotFoundException::new);
    }

    Map<Long, Comment> makeDeleteList(Comment comment) {
        Map<Long, Comment> deleteList = new HashMap<>();

        if (comment.getReplies() != null) {
            comment.getReplies().forEach((c) -> {
                Map<Long, Comment> temp = makeDeleteList(c);
                deleteList.putAll(temp);
            });
        }
        deleteList.put(comment.getId(), comment);

        return deleteList;
    }

    private Comment checkAuth(Long clubId, Long postId, Long commentId, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());
        Comment comment = commentRepository.findById(commentId).orElseThrow();
                findById(clubId, postId, commentId);

        if (!comment.getUser().getId().equals(user.getId()) && !clubMember.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new UnAuthorizedModifyException();
        }

        return comment;
    }
}
