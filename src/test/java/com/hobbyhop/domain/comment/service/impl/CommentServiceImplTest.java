package com.hobbyhop.domain.comment.service.impl;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("댓글 테스트 목록")
class CommentServiceImplTest {
//    private String CONTENT;
//    private UserTest userTest;
//    private Long clubId;
//    private Long postId;
//    @Autowired
//    private CommentService commentService;
//
//
//    @BeforeEach
//    void setUp() {
//        CONTENT = "testContent";
//        postId = 1L;
//        clubId = 1L;
//    }
//
//    @Test
//    @Order(0)
//    @DisplayName("댓글 등록")
//    void postComment(){
//        // give
//        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
//                .content(CONTENT)
//                .build();
//        // when
//        commentService.postComment(requestDTO, clubId, postId, userTest.TEST_USER);
//
//        // then
//        assertThat(commentService.findById(1L).getId()).isNotNull();
//    }
//
//    @Test
//    @Order(1)
//    @DisplayName("대댓글 등록")
//    void PostReply(){
//        // give
//        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
//                .content(CONTENT)
//                .build();
//        // when
//        commentService.postComment(requestDTO, postId, 1L, userTest.TEST_USER);
//
//        // then
//        assertThat(commentService.findById(2L).getId()).isNotNull();
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("댓글 수정")
//    void patchComment() {
//        // given
//        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
//                .content(CONTENT)
//                .build();
//        // when
//        commentService.patchComment(requestDTO,clubId, 1L, userTest.TEST_USER);
//        // then
//        assertThat(commentService.findById(2L).getContent()).isEqualTo(CONTENT);
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("댓글 삭제")
//    void deleteComment() {
//        // given & when
//        commentService.deleteComment(clubId, 2L, userTest.TEST_USER);
//        // then
//        assertThat(commentService.findById(2L)).isNull();
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("댓글 가져오기")
//    void getComments() {
//        // given
//        Pageable pageable = PageRequest.of(0, 5);
//        SortStandardRequest standard = SortStandardRequest.builder()
//                .sortStandard(1)
//                .desc(false)
//                .build();
//        // when
//        CommentListResponseDTO dto = commentService.getComments(pageable, standard, postId);
//
//        // then
//        assertThat(dto.getData().size())
//                .isLessThan(5)
//                .isNotNull();
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("좋아요 설정")
//    void likeComment() {
//        int like = commentService.getLike(commentService.findById(1L));
//        // given & when
//        commentService.likeComment(1L, userTest.TEST_USER);
//        // then
//        assertThat(like + 1).isEqualTo(commentService.getLike(commentService.findById(1L)));
//    }
}