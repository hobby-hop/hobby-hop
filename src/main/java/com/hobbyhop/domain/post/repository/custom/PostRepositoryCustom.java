package com.hobbyhop.domain.post.repository.custom;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    PostPageResponseDTO findAllByClubId(Pageable pageable, Long clubId);
}
