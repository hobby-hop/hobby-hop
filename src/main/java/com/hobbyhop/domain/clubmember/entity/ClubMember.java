package com.hobbyhop.domain.clubmember.entity;

import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import jakarta.persistence.*;
import com.hobbyhop.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE club_member SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(name="deleted_at")
    private Timestamp deletedAt;
}
