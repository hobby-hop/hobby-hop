package com.hobbyhop.domain.post.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG;

import com.hobbyhop.domain.club.service.impl.ClubServiceImpl;
import com.hobbyhop.domain.clubmember.service.impl.ClubMemberServiceImpl;
import com.hobbyhop.domain.post.dto.PostModifyRequestDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.s3.S3Service;
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
    private PostRepository postRepository;
    @Mock
    private ClubServiceImpl clubServiceImpl;
    @Mock
    private ClubMemberServiceImpl clubMemberService;
    @Mock
    private S3Service s3Service;

    private PostRequestDTO postRequestDTO;
    private PostResponseDTO postResponseDTO;
    private PostModifyRequestDTO postModifyRequestDTO;

    @BeforeEach
    public void setup() {

        postRequestDTO = PostRequestDTO.builder()
                .postTitle(TEST_POST_TITLE)
                .postContent(TEST_POST_CONTENT)
                .build();

        postResponseDTO = PostResponseDTO.fromEntity(TEST_POST);

        postModifyRequestDTO = PostModifyRequestDTO.builder()
                .postTitle(TEST_POST_TITLE)
                .postContent(TEST_POST_CONTENT)
                .build();
    }

    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void 게시글생성테스트() {

        // given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(clubServiceImpl.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(postRepository.save(any())).willReturn(TEST_POST);

        // when - then
        assertThat(postServiceImpl.makePost(TEST_USER, TEST_CLUB_ID, postRequestDTO)
                .getPostTitle()).isEqualTo(TEST_POST_TITLE);
        assertThat(postServiceImpl.makePost(TEST_USER, TEST_CLUB_ID, postRequestDTO)
                .getPostContent()).isEqualTo(TEST_POST_CONTENT);

    }

    @Test
    @DisplayName("이미지 업로드 성공 테스트")
    void 이미지업로드테스트() {

        // given
        String originalImageUrl = "testUrl";
        String savedImageUrl = "testSavedUrl";

        // when
        TEST_POST.changeImageUrl(originalImageUrl, savedImageUrl);

        // then
        assertThat(TEST_POST.getOriginImageUrl()).isEqualTo(originalImageUrl);
        assertThat(TEST_POST.getSavedImageUrl()).isEqualTo(savedImageUrl);
    }

    @Test
    @DisplayName("게시글 단건 조회 성공 테스트")
    void 게시글단건조회테스트() {

        // given
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(TEST_POST));

        // when - then
        assertThat(postServiceImpl.findPost(TEST_POST_ID).getId()).isEqualTo(
                postResponseDTO.getPostId());
        assertThat(postServiceImpl.findPost(TEST_POST_ID).getPostTitle()).isEqualTo(
                postResponseDTO.getPostTitle());
        assertThat(postServiceImpl.findPost(TEST_POST_ID).getPostContent()).isEqualTo(
                postResponseDTO.getPostContent());
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void 게시글수정테스트() throws IOException {

        // given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(clubServiceImpl.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(TEST_POST));

        String fileUrl = "images/image1.jpg";
        Resource fileResource = new ClassPathResource(fileUrl);
        MockMultipartFile multipartFile =
                new MockMultipartFile(
                        "image1",
                        fileResource.getFilename(),
                        IMAGE_JPEG.getType(),
                        fileResource.getInputStream());

        // when - then
        assertThat(postServiceImpl.modifyPost(TEST_USER, TEST_CLUB_ID, TEST_POST_ID, multipartFile,
                postModifyRequestDTO).getPostTitle()).isEqualTo(postResponseDTO.getPostTitle());

    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void 게시글삭제테스트() {

        // given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(clubServiceImpl.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(TEST_POST));

        // when
        postServiceImpl.deletePost(TEST_USER, TEST_CLUB_ID, TEST_POST_ID);

        // then
        verify(postRepository, times(1)).deleteAllElement(TEST_POST_ID);
    }
}

