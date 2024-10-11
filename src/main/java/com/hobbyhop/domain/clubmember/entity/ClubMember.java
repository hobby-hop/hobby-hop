package com.hobbyhop.domain.clubmember.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.domain.clubmember.enums.MemberRole;
import com.hobbyhop.domain.clubmember.pk.ClubMemberPK;
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
public class ClubMember extends BaseEntity {
    @EmbeddedId
    private ClubMemberPK clubMemberPK;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public static ClubMember buildClubMember(Club club, User user, MemberRole memberRole) {
        return ClubMember.builder()
                .clubMemberPK(ClubMemberPK.builder()
                        .club(club)
                        .user(user)
                        .build())
                .memberRole(memberRole).build();
    }
}