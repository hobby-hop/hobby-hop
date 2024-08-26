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
import com.hobbyhop.domain.post.dto.PostPageRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.s3.S3Service;
import com.hobbyhop.domain.postuser.repository.PostUserRepository;
import com.hobbyhop.domain.postuser.service.impl.PostUserServiceImpl;
import com.hobbyhop.test.CategoryTest;
import com.hobbyhop.test.ClubTest;
import com.hobbyhop.test.PostTest;
import com.hobbyhop.test.UserTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Post]")
class PostServiceImplTest implements PostTest, UserTest, CategoryTest, ClubTest {
    @InjectMocks
    private PostServiceImpl sut;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostUserServiceImpl postUserService;
    @Mock
    private PostUserRepository postUserRepository;
    @Mock
    private ClubServiceImpl clubServiceImpl;
    @Mock
    private ClubMemberServiceImpl clubMemberService;
    @Mock
    private S3Service s3Service;

    private PostRequestDTO postRequestDTO;
    private PostResponseDTO postResponseDTO;
    private PostModifyRequestDTO postModifyRequestDTO;
    private PostPageRequestDTO postPageRequestDTO;

    @BeforeEach
    public void setup() {
        postRequestDTO = PostRequestDTO.builder()
                .title(TEST_POST_TITLE)
                .content(TEST_POST_CONTENT)
                .build();

        postResponseDTO = PostResponseDTO.fromEntity(TEST_POST);

        postModifyRequestDTO = PostModifyRequestDTO.builder()
                .title(TEST_POST_TITLE)
                .content(TEST_POST_CONTENT)
                .build();

        postPageRequestDTO = PostPageRequestDTO.builder()
                .build();
    }

    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void 게시글생성테스트() {
        // Given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(clubServiceImpl.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(postRepository.save(any())).willReturn(TEST_POST);

        // When & Then
        assertThat(sut.makePost(TEST_USER, TEST_CLUB_ID, postRequestDTO)
                .getTitle()).isEqualTo(TEST_POST_TITLE);
        assertThat(sut.makePost(TEST_USER, TEST_CLUB_ID, postRequestDTO)
                .getContent()).isEqualTo(TEST_POST_CONTENT);

    }

    @Test
    @DisplayName("이미지 업로드 성공 테스트")
    void 이미지업로드테스트() {
        // Given
        String originalImageUrl = "testUrl";
        String savedImageUrl = "testSavedUrl";

        // When
        TEST_POST.changeImageUrl(originalImageUrl, savedImageUrl);

        // Then
        assertThat(TEST_POST.getOriginImageUrl()).isEqualTo(originalImageUrl);
        assertThat(TEST_POST.getSavedImageUrl()).isEqualTo(savedImageUrl);
    }

    @Test
    @DisplayName("게시글 단건 조회 성공 테스트")
    void 게시글단건조회테스트() {
        // Given
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(TEST_POST));

        // When & Then
        assertThat(sut.findPost(TEST_POST_ID).getId()).isEqualTo(
                postResponseDTO.getPostId());
        assertThat(sut.findPost(TEST_POST_ID).getTitle()).isEqualTo(
                postResponseDTO.getTitle());
        assertThat(sut.findPost(TEST_POST_ID).getContent()).isEqualTo(
                postResponseDTO.getContent());
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void 게시글수정테스트() throws IOException {
        // Given
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

        // When & Then
        assertThat(sut.modifyPost(TEST_USER, TEST_CLUB_ID, TEST_POST_ID, multipartFile,
                postModifyRequestDTO).getTitle()).isEqualTo(postResponseDTO.getTitle());

    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void 게시글삭제테스트() {
        // Given
        given(clubMemberService.isClubMember(TEST_CLUB_ID, TEST_USER_ID)).willReturn(true);
        given(clubServiceImpl.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(TEST_POST));

        // When
        sut.deletePost(TEST_USER, TEST_CLUB_ID, TEST_POST_ID);

        // Then
        verify(postRepository, times(1)).deleteAllElement(TEST_POST_ID);
    }

    @Test
    @DisplayName("게시글 좋아요 성공 테스트")
    void 게시글좋아요테스트() {
        // Given
        given(clubServiceImpl.findClub(TEST_CLUB_ID)).willReturn(TEST_CLUB);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(TEST_POST));

        // When
        sut.likePost(TEST_USER, TEST_CLUB_ID, TEST_POST_ID);

        // Then
        assertThat(TEST_POST.getLikeCnt().equals(1L));
    }

    @Test
    @DisplayName("게시글 전체 조회 성공 테스트")
    void 게시글전체조회테스트() {
        // Given
        List<PostPageResponseDTO> content = new ArrayList<>();
        PostPageResponseDTO postPageResponseDTO1 = new PostPageResponseDTO(TEST_CLUB_ID,
                TEST_POST_ID, TEST_USER_NAME,
                TEST_POST_TITLE, TEST_POST_LIKE, TEST_CREATED_AT,
                TEST_MODIFIED_AT);
        content.add(postPageResponseDTO1);
        given(postRepository.findAllByClubId(postPageRequestDTO, TEST_CLUB_ID)).willReturn(
                new PageImpl<>(content, postPageRequestDTO.getPageable(postPageRequestDTO.getSortBy()), 10L));

        // When & Then
        assertThat(sut.getAllPost(postPageRequestDTO, TEST_CLUB_ID).getSize()).isEqualTo(
                10);
    }
}

