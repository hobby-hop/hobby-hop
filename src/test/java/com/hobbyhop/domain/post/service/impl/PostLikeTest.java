package com.hobbyhop.domain.post.service.impl;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.service.CategoryService;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.facade.OptimisticLockPostLikeFacade;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.dto.SignupRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.user.enums.UserRoleEnum;
import com.hobbyhop.domain.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Log4j2
public class PostLikeTest {
    @Autowired
    OptimisticLockPostLikeFacade olpf;
    @Autowired
    UserService userService;
    @Autowired
    ClubService clubService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PostService postService;

    User testuser = User.builder()
            .id(1L)
            .email("testuser1@naver.com")
            .username("testuser1")
            .password("testuser1")
            .role(UserRoleEnum.USER)
            .build();
    SignupRequestDTO userDto = SignupRequestDTO.builder()
            .email("testuser1@naver.com")
            .username("testuser1")
            .password("testuser1")
            .confirmPassword("testuser1")
            .build();
    CategoryRequestDTO categoryDto = CategoryRequestDTO.builder()
            .categoryName("test")
            .description("test")
            .build();
    ClubRequestDTO clubRequestDTO = ClubRequestDTO.builder()
            .categoryId(1L)
            .title("testtt")
            .content("contents")
            .build();
    PostRequestDTO postDto = PostRequestDTO.builder()
            .title("test")
            .content("test")
            .build();

    @BeforeEach
    void test() {
        userService.signup(userDto);
        categoryService.makeCategory(categoryDto);
        clubService.makeClub(clubRequestDTO, testuser);
        postService.writePost(postDto, 1L, testuser);
    }

    @Test
    @DisplayName("낙관적 락을 적용하여 좋아요 동시성 제어 성공")
    void 동시에_100개의_좋아요요청_성공() throws InterruptedException {
        int threadCount = 100;
        Long testClubId = 1L;
        Long testPostId = 1L;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    olpf.likePost(testuser, testClubId, testPostId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Post post = postService.findPost(testClubId, testPostId);
        assertThat(post.getLikeCnt()).isEqualTo(0);
    }
}
