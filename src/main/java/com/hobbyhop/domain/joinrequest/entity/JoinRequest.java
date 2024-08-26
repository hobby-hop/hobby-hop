package com.hobbyhop.domain.joinrequest.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.user.entity.User;
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
@SQLDelete(sql = "UPDATE join_request SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class JoinRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "send_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private JoinRequestStatus status;

    @Column(name="deleted_at")
    private Timestamp deletedAt;

    public void changeStatus(JoinRequestStatus status) {
        this.status = status;
    }
}
