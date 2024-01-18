package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    public void setup() {
        //SignupRequestDTO signRequestDTO = new SignupRequestDTO("testUsername","testEmail@email.com","testPassword","testPasswordConfirm");
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

        String postTitle = "testTitle";
        String postContent= "testContent";
        Long clubId = 1L;
        MockMultipartFile file = new MockMultipartFile("file","test.txt","text/plain","test file".getBytes(
                StandardCharsets.UTF_8));

        //when
//postService.makePost(user, clubId, file, PostRequestDTO.builder()
//                .postContent(postContent)
//                .postTitle(postTitle)
//        .build());

        //then


    }
}
