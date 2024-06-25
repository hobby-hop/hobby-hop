package com.hobbyhop.domain.comment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.domain.comment.dto.CommentPageRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.repository.CommentRepository;
import com.hobbyhop.domain.commentuser.service.impl.CommentUserServiceImpl;
import com.hobbyhop.domain.post.service.impl.PostServiceImpl;
import com.hobbyhop.test.CommentTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest implements CommentTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostServiceImpl postService;
    @Mock
    private CommentUserServiceImpl commentUserService;
    @Mock
    private ClubMemberServiceImpl clubMemberService;
    private CommentRequestDTO requestDTO;
    private CommentRequestDTO modifyDTO;
    private CommentPageRequestDTO pageRequestDTO;

    @BeforeEach
    public void setup() {
        requestDTO = CommentRequestDTO.builder()
                .content(TEST_COMMENT_CONTENT)
                .build();

        modifyDTO = CommentRequestDTO.builder()
                .content(TEST_OTHER_COMMENT_CONTENT)
                .build();

        pageRequestDTO = CommentPageRequestDTO.builder()
                .page(1)
                .size(10)
                .isDesc(true)
                .standard("standard")
                .build();
    }

    @Test
    @DisplayName("댓글 생성 성공 테스트")
    void 댓글생성테스트() {
        // given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(postService.findPost(TEST_POST_ID)).willReturn(TEST_POST);
        given(commentRepository.save(any())).willReturn(TEST_COMMENT);

        // when & then
        assertThat(commentService.postComment(requestDTO, TEST_CLUB_ID, TEST_POST_ID, TEST_USER)
                .getContent()).isEqualTo(TEST_COMMENT.getContent());
    }

    @Test
    @DisplayName("대댓글 생성 성공 테스트")
    void 대댓글생성테스트() {
        // given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(postService.findPost(TEST_POST_ID)).willReturn(TEST_POST);
        given(commentRepository.save(any())).willReturn(TEST_OTHER_COMMENT);
        requestDTO.setContent(TEST_OTHER_COMMENT_CONTENT);

        // when & then
        assertThat(commentService.postComment(requestDTO, TEST_CLUB_ID, TEST_POST_ID, TEST_USER)
                .getContent()).isEqualTo(TEST_OTHER_COMMENT.getContent());
    }

    @Test
    @DisplayName("댓글 수정 완료 테스트")
    void 댓글수정테스트() {
        // given
        given(commentRepository.findById(TEST_CLUB_ID, TEST_USER_ID, TEST_COMMENT_ID)).willReturn(
                Optional.ofNullable(TEST_COMMENT));

        // when & then
        assertThat(
                commentService.patchComment(modifyDTO, TEST_CLUB_ID, TEST_POST_ID, TEST_COMMENT_ID,
                        TEST_USER).getContent()).isEqualTo(TEST_OTHER_COMMENT_CONTENT);
    }

    @Test
    @DisplayName("댓글 삭제 완료 테스트")
    void 댓글삭제테스트() {
        // given
        given(commentRepository.findById(TEST_CLUB_ID, TEST_USER_ID, TEST_COMMENT_ID)).willReturn(
                Optional.ofNullable(TEST_COMMENT));

        // when
        commentService.deleteComment(TEST_CLUB_ID, TEST_POST_ID, TEST_COMMENT_ID, TEST_USER);

        // then
        verify(commentRepository, times(1)).deleteList(
                commentService.makeDelete(Objects.requireNonNull(TEST_COMMENT)).values().stream().toList());
    }

//    @Test
//    @DisplayName("댓글 조회 완료 테스트")
//    void 댓글조회테스트() {
//        // given
//        List<CommentResponseDTO> content = new ArrayList<>();
//        CommentResponseDTO responseDTO = CommentResponseDTO.buildDTO(TEST_COMMENT);
//        content.add(responseDTO);
//
//        given(commentRepository.findAllByPostId(pageRequestDTO, TEST_POST_ID,
//                TEST_COMMENT_ID)).willReturn(
//                new PageImpl<>(content, pageRequestDTO.getPageable("id"), 1L));
//
//        // when & then
//        assertThat(commentService.getComments(pageRequestDTO, TEST_POST_ID, TEST_COMMENT_ID)
//                .getDtoList().get(0).getContent()).isEqualTo(TEST_COMMENT_CONTENT);
//    }

    @Test
    @DisplayName("댓글 좋아요 완료 테스트")
    void 댓글좋아요테스트() {
        // given
        given(commentRepository.findById(TEST_CLUB_ID, TEST_USER_ID, TEST_COMMENT_ID)).willReturn(Optional.ofNullable(TEST_COMMENT));

        // when
        commentService.likeComment(TEST_CLUB_ID, TEST_POST_ID, TEST_COMMENT_ID, TEST_USER);

        // then
        verify(commentUserService).modifyCommentUser(
                Objects.requireNonNull(TEST_COMMENT), TEST_USER);
    }
}