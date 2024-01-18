package com.hobbyhop.domain.joinrequest.dto;

import com.hobbyhop.domain.joinrequest.enums.JoinRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO {
    private JoinRequestStatus status;
}
