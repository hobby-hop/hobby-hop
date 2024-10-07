package com.hobbyhop.domain.joinrequest.dto;

import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO {
    private JoinRequestStatus status;
}
