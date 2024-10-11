package com.hobbyhop.domain.club.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 30)
    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public static Club buildClub(ClubRequestDTO clubRequestDTO, Category category) {
        return Club.builder()
                .title(clubRequestDTO.getTitle())
                .content(clubRequestDTO.getContent())
                .category(category).build();
    }
}
