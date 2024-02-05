package com.hobbyhop.domain.joinrequest.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

//유저가 모임에 초대를 누르면 리퀘스트 테이블에 기록된다.
//유저쪽과 연계해서 알람 목록에서 허락을 누르면 그 때 멤버 테이블에 기록한다.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
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
