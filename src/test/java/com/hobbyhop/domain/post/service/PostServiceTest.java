package com.hobbyhop.domain.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.repository.UserRepository;
import com.hobbyhop.domain.user.service.UserService;
import com.hobbyhop.test.CategoryTest;
import com.hobbyhop.test.ClubTest;
import com.hobbyhop.test.PostTest;
import com.hobbyhop.test.UserTest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements PostTest, UserTest, CategoryTest, ClubTest {

    @Mock UserService userService;
    @Mock PostService postService;
    @Mock UserRepository userRepository;
    @Mock PostRepository postRepository;
    User user;
    Category category;
    Club club;

    @BeforeEach
    public void setup() {
         user = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("testEmail@email.com")
                .build();

         category = Category.builder()
                 .categoryName("")
                 .build();

//
//        userRepository.save(user);
//        SignupRequestDTO signRequestDTO = new SignupRequestDTO("Test","Tests");
//        userService.signup(signRequestDTO);
    }

    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void test1(){

        //given
        User user = User.builder()
                .username("testUser")
                .password("testPassword")
                .email("testEmail@email.com")
                .build();
        //User user = userRepository.findByUsername("testUser").orElseThrow();

        String testPostTitle = "testTitle";
        String testPostContent= "testContent";
        Long clubId = 1L;
        MockMultipartFile file = new MockMultipartFile("file","test.txt","text/plain","test file".getBytes(
                StandardCharsets.UTF_8));

        //when
        PostResponseDTO postResponseDTO = postService.makePost(user, clubId, PostRequestDTO.builder()
                .postContent(testPostTitle)
                .postTitle(testPostContent)
        .build());

        //then
        assertNotNull(postResponseDTO.getPostId());
        assertEquals(testPostTitle, postResponseDTO.getPostTitle());
        assertEquals(testPostContent,  postResponseDTO.getPostContent());

    }
}
