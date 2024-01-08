package com.hobbyhop.domain.club.entity;

import com.hobbyhop.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE club SET deleted_at = NOW() where id=?")
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Column(name="deleted_at")
    private Timestamp deletedAt;

    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String content) {
        this.content = content;
    }

}
