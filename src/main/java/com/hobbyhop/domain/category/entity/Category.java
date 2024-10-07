package com.hobbyhop.domain.category.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
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
@SQLDelete(sql = "UPDATE category SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "category_name")
    private String categoryName;

    private String description;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public static Category buildCategory(CategoryRequestDTO categoryRequestDTO) {
        return Category.builder()
                .categoryName(categoryRequestDTO.getCategoryName())
                .description(categoryRequestDTO.getDescription())
                .build();
    }
}
