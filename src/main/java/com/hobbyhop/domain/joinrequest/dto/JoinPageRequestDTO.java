package com.hobbyhop.domain.joinrequest.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class JoinPageRequestDTO extends PageRequestDTO {
}
