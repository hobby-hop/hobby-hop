package com.hobbyhop.domain.comment.service.impl;

import com.hobbyhop.domain.clubmember.entity.ClubMember;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.comment.dto.CommentListResponseDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.CommentRepository;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.comment.CommentNotFoundException;
import com.hobbyhop.global.exception.common.UnAuthorizedModifyException;
import com.hobbyhop.global.request.SortStandardRequest;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentUserService commentUserService;
    private final ClubMemberService clubMemberService;

    @Override
    public CommentResponseDTO postComment(CommentRequestDTO request, Long clubId, Long postId, User user) {
        if(clubMemberService.isClubMember(clubId, user.getId()))
            throw new ClubMemberNotFoundException();

        Post post = postService.findPost(postId);

        Comment comment = buildComment(request, post, user, null);

        commentRepository.save(comment);

        return CommentResponseDTO.buildDTO(comment, getLike(comment));
    }

    @Override
    public CommentResponseDTO postComment(CommentRequestDTO request, Long clubId, Long postId, Long commentId, User user) {
        if(clubMemberService.isClubMember(clubId, user.getId()))
            throw new ClubMemberNotFoundException();

        Post post = postService.findPost(postId);
        // 저장 되어 있는 상위 댓글 가져 오기
        Comment comment = findById(clubId, postId, commentId); 

        Comment reply = buildComment(request, post, user, comment);

        commentRepository.save(comment);

        // 상위 댓글에 리플 추가
        comment.getReply().add(reply);

        return CommentResponseDTO.buildDTO(comment, getLike(comment));
    }

    @Override
    @Transactional
    public void patchComment(CommentRequestDTO requestDto, Long clubId, Long postId, Long commentId, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());

        Comment comment = findById(clubId, postId, commentId); 

        if(!comment.getUser().getId().equals(user.getId()) && !clubMember.getMemberRole().equals(MemberRole.ADMIN))
            throw new UnAuthorizedModifyException();

        comment.changeContent(requestDto.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long clubId, Long postId, Long commentId, User user) {
        ClubMember clubMember = clubMemberService.findByClubAndUser(clubId, user.getId());

        Comment comment = findById(clubId, postId, commentId); 

        if(!comment.getUser().getId().equals(user.getId()) && !clubMember.getMemberRole().equals(MemberRole.ADMIN))
            throw new UnAuthorizedModifyException();

        // 본인과 하위 리플의 아이디 값을 저장할 리스트 생성
        Map<Long, Comment> deleteList = makeDelete(comment);

        commentRepository.deleteList(deleteList.values().stream().toList());
    }

    @Override
    public CommentListResponseDTO getComments(Pageable pageable, SortStandardRequest standard, Long postId) {
        return commentRepository.findAllByPostId(pageable, standard, postId);
    }

    @Override
    public void likeComment(Long clubId, Long postId, Long commentId, User user) {
        Comment comment = findById(clubId, postId, commentId); 
        commentUserService.modifyCommentUser(comment, user);
    }

    private Comment findById(Long clubId, Long postId, Long commentId) {
        return commentRepository.findById(clubId, postId, commentId).orElseThrow(CommentNotFoundException::new);
    }

    private Comment buildComment(CommentRequestDTO request, Post post, User user, Comment comment){
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .parent(comment)
                .reply(new ArrayList<>())
                .build();
    }

    private int getLike(Comment comment){
        return commentUserService.countLike(comment);
    }

    private Map<Long, Comment> makeDelete(Comment comment){
        Map<Long, Comment> deleteList = new HashMap<>();

        if (!comment.getReply().isEmpty()){
            comment.getReply().forEach((c) -> {
                Map<Long, Comment> temp = makeDelete(c);
                deleteList.putAll(temp);
            });
        }
        deleteList.put(comment.getId(), comment);

        return deleteList;
    }
}
