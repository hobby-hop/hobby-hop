package com.hobbyhop.domain.category.entity;

import com.hobbyhop.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@SQLDelete(sql = "UPDATE category SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "category_name")
    private String categoryName;

    private String description;

    @Column(name="deleted_at")
    private Timestamp deletedAt;
}
