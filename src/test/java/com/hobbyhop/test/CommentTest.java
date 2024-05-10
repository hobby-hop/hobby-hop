package com.hobbyhop.test;

import com.hobbyhop.domain.comment.entity.Comment;

public interface CommentTest extends PostTest{

    Long TEST_COMMENT_ID = 1L;
    Long TEST_COMMENT_LIKE = 1L;
    String TEST_COMMENT_CONTENT = "content";


    Long TEST_OTHER_COMMENT_ID = 2L;
    Long TEST_OTHER_COMMENT_LIKE = 2L;
    String TEST_OTHER_COMMENT_CONTENT = "otherContent";

    Comment TEST_COMMENT =
            Comment.builder()
                    .id(TEST_COMMENT_ID)
                    .user(TEST_USER)
                    .post(TEST_POST)
                    .likeCnt(TEST_COMMENT_LIKE)
                    .content(TEST_COMMENT_CONTENT)
                    .parent(null)
                    .build();

    Comment TEST_OTHER_COMMENT =
            Comment.builder()
                    .id(TEST_OTHER_COMMENT_ID)
                    .user(TEST_OTHER_USER)
                    .post(TEST_POST)
                    .likeCnt(TEST_OTHER_COMMENT_LIKE)
                    .content(TEST_OTHER_COMMENT_CONTENT)
                    .parent(TEST_COMMENT)
                    .build();
}
