package com.hobbyhop.domain.club.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE club SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name="deleted_at")
    private Timestamp deletedAt;

    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String content) {
        this.content = content;
    }
    public void changeCategory(Category category) {
        this.category = category;
    }

}
