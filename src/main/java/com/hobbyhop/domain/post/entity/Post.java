package com.hobbyhop.domain.post.entity;


import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postTitle;
    private String postContent;

    @Column(length = 1000)
    private String originImageUrl;
    @Column(length = 1000)
    private String savedImageUrl;

    @Column(nullable = false)
    private Long likeCnt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public void changeTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void changeContent(String postContent) {
        this.postContent = postContent;
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
}