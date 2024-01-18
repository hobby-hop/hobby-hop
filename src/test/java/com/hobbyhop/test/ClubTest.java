package com.hobbyhop.test;

import com.hobbyhop.domain.club.entity.Club;

public interface ClubTest extends CategoryTest{

    Long TEST_CLUB_ID = 1L;
    String TEST_CLUB_TITLE = "club";
    String TEST_CLUB_CONTENT = "content";

    Long TEST_OTHER_CLUB_ID = 2L;
    String TEST_OTHER_CLUB_TITLE = "other";
    String TEST_OTHER_CLUB_CONTENT = "otherContent";

    Club TEST_CLUB =
            Club.builder()
                    .id(TEST_CLUB_ID)
                    .title(TEST_CLUB_TITLE)
                    .content(TEST_CLUB_CONTENT)
                    .build();

    Club TEST_OTHER_CLUB =
            Club.builder()
                    .id(TEST_OTHER_CLUB_ID)
                    .title(TEST_OTHER_CLUB_TITLE)
                    .content(TEST_OTHER_CLUB_CONTENT)
                    .build();
}