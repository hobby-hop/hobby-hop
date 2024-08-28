package com.hobbyhop.domain.comment.service.impl;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.comment.dto.CommentPageRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.CommentRepository;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.auth.UnAuthorizedModifyException;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.comment.CommentNotFoundException;
import com.hobbyhop.global.response.PageResponseDTO;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentUserService commentUserService;
    private final PostService postService;
    private final ClubMemberService clubMemberService;

    @Override
    public CommentResponseDTO writeComment(CommentRequestDTO request, Long clubId, Long postId, User user) {
        if (!clubMemberService.isClubMember(clubId, user.getId()))
            throw new ClubMemberNotFoundException();

        Post post = postService.findPost(postId);
        Comment comment = Comment.buildComment(request, post, user, null);
        commentRepository.save(comment);

        return CommentResponseDTO.fromEntity(comment);
    }

    @Override
    public CommentResponseDTO writeReply(CommentRequestDTO request, Long clubId, Long postId, Long commentId, User user) {
        if (!clubMemberService.isClubMember(clubId, user.getId()))
            throw new ClubMemberNotFoundException();

        Post post = postService.findPost(postId);
        Comment comment = findById(clubId, postId, commentId);
        Comment reply = Comment.buildComment(request, post, user, comment);
        commentRepository.save(reply);
        comment.getReply().add(reply);

        return CommentResponseDTO.fromEntity(comment);
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
    public PageResponseDTO<CommentResponseDTO> getComments(CommentPageRequestDTO pageRequestDTO, Long postId, Long commentId) {
        Page<CommentResponseDTO> result = commentRepository.findAllByPostId(pageRequestDTO, postId, commentId);

        return PageResponseDTO.<CommentResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.toList())
                .total(result.getTotalElements())
                .build();
    }

    @Override
    public void likeComment(Long clubId, Long postId, Long commentId, User user) {
        Comment comment = findById(clubId, postId, commentId);
        commentUserService.modifyCommentUser(comment, user);
    }

    @Override
    public Comment findById(Long clubId, Long postId, Long commentId) {
        return commentRepository.findById(clubId, postId, commentId).orElseThrow(CommentNotFoundException::new);
    }

    Map<Long, Comment> makeDeleteList(Comment comment) {
        Map<Long, Comment> deleteList = new HashMap<>();

        if (comment.getReply() != null) {
            comment.getReply().forEach((c) -> {
                Map<Long, Comment> temp = makeDeleteList(c);
                deleteList.putAll(temp);
            });
        }
        deleteList.put(comment.getId(), comment);

        return deleteList;
    }

    Comment checkAuth(Long clubId, Long postId, Long commentId, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());
        Comment comment = findById(clubId, postId, commentId);

        if (!comment.getUser().getId().equals(user.getId()) && !clubMember.getMemberRole().equals(MemberRole.ADMIN))
            throw new UnAuthorizedModifyException();

        return comment;
    }
}
