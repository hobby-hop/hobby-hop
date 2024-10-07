package com.hobbyhop.domain.post.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.postimage.entity.PostImage;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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

    @Column(nullable = false)
    private Long likeCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "post",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @Builder.Default
    private Set<PostImage> imageSet = new HashSet<>();

    @Version
    private Long version;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void addLikeCnt() {
        this.likeCnt++;
    }

    public void subLikeCnt() {
        this.likeCnt--;
    }

    public static Post buildPost(PostRequestDTO postRequestDTO, Club club, User user) {
        Post post = Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .club(club)
                .user(user)
                .likeCnt(0L)
                .build();

        if(postRequestDTO.getFileNames() != null) {
            postRequestDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.getSavedFileName().split("_");
                post.addImage(arr[0], arr[1], fileName.getSavedFileUrl());
            });
        }

        return post;
    }

    public void addImage(String uuid, String fileName, String savedFileUrl) {
        PostImage postImage = PostImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .savedFileUrl(savedFileUrl)
                .post(this)
                .build();

        this.imageSet.add(postImage);
    }

    public void clearImages() {
        imageSet.forEach(postImage -> {
            postImage.changePost(null);
            this.imageSet.clear();
        });
    }
}