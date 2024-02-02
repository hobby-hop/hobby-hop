package com.hobbyhop.test;

import com.hobbyhop.domain.user.entity.User;

public interface UserTest {

    Long TEST_USER_ID = 1L;
    Long TEST_USER_KAKAO_ID = 1L;

    Long TEST_OTHER_USER_ID = 2L;
    Long TEST_OTHER_USER_KAKAO_ID = 2L;

    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "12345678";
    String TEST_USER_CONFIRM_PASSWORD = "12345678";
    String TEST_USER_EMAIL = "test@email.com";
    String TEST_USER_ROLE = "USER";

    String TEST_OTHER_USER_NAME = "othername";
    String TEST_OTHER_USER_PASSWORD = "87654321";
    String TEST_OTHER_USER_CONFIRM_PASSWORD = "87654321";
    String TEST_OTHER_USER_EMAIL = "other@email.com";
    String TEST_OTHER_USER_ROLE = "USER";

    User TEST_USER =
            User.builder()
                    .id(TEST_USER_ID)
                    .username(TEST_USER_NAME)
                    .password(TEST_USER_PASSWORD)
                    .email(TEST_USER_EMAIL)
                    .build();

    User TEST_OTHER_USER =
            User.builder()
                    .id(TEST_USER_ID)
                    .username(TEST_OTHER_USER_NAME)
                    .password(TEST_OTHER_USER_PASSWORD)
                    .email(TEST_OTHER_USER_EMAIL)
                    .build();
}
