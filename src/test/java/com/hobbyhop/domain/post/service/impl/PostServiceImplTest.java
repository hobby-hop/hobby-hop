package com.hobbyhop.domain.post.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.impl.ClubServiceImpl;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.s3.S3Service;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.test.CategoryTest;
import com.hobbyhop.test.ClubTest;
import com.hobbyhop.test.PostTest;
import com.hobbyhop.test.UserTest;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest implements PostTest, UserTest, CategoryTest, ClubTest {

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ClubServiceImpl clubServiceImpl;
    @Mock
    private ClubMemberServiceImpl clubMemberService;
    @Mock
    private S3Service s3Service;
    private Category category;
    private Club club;
    private Post post;


    @BeforeEach
    public void setup() {

        category = Category.builder()
                .id(TEST_CATEGORY_ID)
                .categoryName(TEST_CATEGORY_NAME)
                .description(TEST_CATEGORY_DESCRIPTION)
                .build();

        club = Club.builder()
                .id(TEST_CLUB_ID)
                .category(category)
                .title(TEST_CLUB_TITLE)
                .content(TEST_CLUB_CONTENT)
                .build();

        post = Post.builder()
                .id(TEST_POST_ID)
                .postTitle(TEST_POST_TITLE)
                .postContent(TEST_POST_CONTENT)
                .user(TEST_USER)
                .club(club)
                .build();
    }

    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void 게시글생성테스트() {
//
//        //given
//        String testPostTitle = "testTitle";
//        String testPostContent = "testContent";
//        Long clubId = 1L;
//        clubMemberService.joinClub(TEST_CLUB,TEST_USER);
//
//
//        //when
//        PostResponseDTO postResponseDTO = postServiceImpl.makePost(TEST_USER, clubId,
//                PostRequestDTO.builder()
//                        .postTitle(testPostTitle)
//                        .postContent(testPostContent)
//                        .build());
//
//        //then
//        assertThat(testPostTitle).isEqualTo(postResponseDTO.getPostTitle());
//
//        assertThat(postServiceImpl.makePost(TEST_USER, clubId, PostRequestDTO.builder()
//                .postContent(testPostContent)
//                .postTitle(testPostTitle)
//                .build())).isEqualTo(postResponseDTO);
    }

    @Test
    @DisplayName("이미지 업로드 성공 테스트")
    void 이미지업로드테스트() {

        //given
        String originalImageUrl = "testUrl";
        String savedImageUrl = "testSavedUrl";

        //when
        TEST_POST.changeImageUrl(originalImageUrl, savedImageUrl);

        //then
        assertThat(TEST_POST.getOriginImageUrl()).isEqualTo(originalImageUrl);
        assertThat(TEST_POST.getSavedImageUrl()).isEqualTo(savedImageUrl);
    }

    @Test
    @DisplayName("게시글 단건 조회 성공 테스트")
    void 게시글단건조회테스트() {

        //given
        when(clubServiceImpl.findClub(any())).thenReturn(club);
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        //when
        PostResponseDTO postResponseDTO = postServiceImpl.getPostById(TEST_CLUB_ID, TEST_POST_ID);

        //then
        assertThat(postResponseDTO.getPostId()).isEqualTo(TEST_POST_ID);
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    // 파일을 넣어주는 경로가 로컬마다 다르기때문에 주석처리.
    void 게시글수정테스트() throws IOException {

        //given
//        String testPostTitle = "test";
//        String testPostContent = "test";
//
//        String fileUrl = "file:///C:/Users/wkdeh/OneDrive/Pictures/images.IMG.jfif";
//        Resource fileResource = new ClassPathResource(fileUrl);
//        MockMultipartFile multipartFile =
//                new MockMultipartFile(
//                        "image1",
//                        fileResource.getFilename(),
//                        IMAGE_JPEG.getType(),
//                        fileResource.getInputStream());
//
//        //when
//        PostResponseDTO postResponseDTO = postServiceImpl.modifyPost(TEST_OTHER_USER,TEST_OTHER_CLUB_ID,TEST_OTHER_POST_ID,multipartFile,
//                PostRequestDTO.builder()
//                        .postTitle(testPostTitle)
//                        .postContent(testPostContent)
//                        .build());
//
//        //then
//        assertThat(postResponseDTO.getPostTitle()).isEqualTo(testPostTitle);
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void 게시글삭제테스트() {
//
//        //given
//        given(postRepository.findById(any())).willReturn(Optional.of(TEST_POST));
//        given(clubServiceImpl.findClub(any())).willReturn(TEST_CLUB);
//        given(postRepository.save(any())).willReturn(TEST_OTHER_POST);
//        clubMemberService.joinClub(TEST_CLUB,TEST_USER);
//
//        //when
//        postRepository.save(post);
//        postServiceImpl.deletePost(TEST_USER, TEST_CLUB_ID, post.getId());
//
//        //then
//        verify(postRepository, times(1)).deleteById(TEST_POST_ID);
    }
}