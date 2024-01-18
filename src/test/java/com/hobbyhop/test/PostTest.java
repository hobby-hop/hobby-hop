package com.hobbyhop.test;

import com.hobbyhop.domain.post.entity.Post;
import jakarta.persistence.Column;

public interface PostTest extends ClubTest{

    Long TEST_POST_ID = 1L;
    Long TEST_POST_LIKE = 1L;
    String TEST_POST_TITLE = "post";
    String TEST_POST_CONTENT = "content";


    Long TEST_OTHER_POST_ID = 2L;
    Long TEST_OTHER_POST_LIKE = 2L;
    String TEST_OTHER_POST_TITLE = "other";
    String TEST_OTHER_POST_CONTENT = "otherContent";

    Post TEST_POST =
            Post.builder()
                    .id(TEST_POST_ID)
                    .user(TEST_USER)
                    .club(TEST_CLUB)
                    .likeCnt(TEST_POST_LIKE)
                    .postTitle(TEST_POST_TITLE)
                    .postContent(TEST_POST_CONTENT)
                    .build();

    Post TEST_OTHER_POST =
            Post.builder()
                    .id(TEST_OTHER_POST_ID)
                    .user(TEST_OTHER_USER)
                    .club(TEST_OTHER_CLUB)
                    .likeCnt(TEST_OTHER_POST_LIKE)
                    .postTitle(TEST_OTHER_POST_TITLE)
                    .postContent(TEST_OTHER_POST_CONTENT)
                    .build();
}