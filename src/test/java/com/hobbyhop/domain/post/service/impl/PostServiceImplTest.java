package com.hobbyhop.domain.post.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.service.impl.ClubServiceImpl;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.s3.S3Service;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.test.CategoryTest;
import com.hobbyhop.test.ClubTest;
import com.hobbyhop.test.PostTest;
import com.hobbyhop.test.UserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private S3Service s3Service;
    private Category category;
    private Club club;


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
    }

    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void makePostTest() {

        //given
        String testPostTitle = "testTitle";
        String testPostContent = "testContent";
        Long clubId = 1L;

        //when
        PostResponseDTO postResponseDTO = postServiceImpl.makePost(TEST_USER, clubId,
                PostRequestDTO.builder()
                        .postTitle(testPostTitle)
                        .postContent(testPostContent)
                        .build());

        //then
        assertThat(testPostTitle).isEqualTo(postResponseDTO.getPostTitle());

        assertThat(postServiceImpl.makePost(TEST_USER, clubId, PostRequestDTO.builder()
                .postContent(testPostTitle)
                .postTitle(testPostContent)
                .build())).isEqualTo(postResponseDTO);
    }
}
