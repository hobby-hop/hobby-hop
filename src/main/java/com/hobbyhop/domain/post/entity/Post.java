package com.hobbyhop.domain.post.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.sql.results.graph.EntityGraphTraversalState;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(length = 1000)
    private String content;

    @Column(length = 500)
    private String originImageUrl;

    @Column(length = 500)
    private String savedImageUrl;

    @Column(nullable = false)
    private Long likeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeImageUrl(String originImageUrl, String savedImageUrl) {
        this.originImageUrl = originImageUrl;
        this.savedImageUrl = savedImageUrl;
    }

    public void updateLikeCnt(Boolean updated) {
        if (updated) {
            this.likeCnt++;
            return;
        }
        this.likeCnt--;
    }
    public static Post buildPost(PostRequestDTO postRequestDTO, Club club, User user) {
        return Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .club(club)
                .user(user)
                .likeCnt(0L)
                .build();
    }
}