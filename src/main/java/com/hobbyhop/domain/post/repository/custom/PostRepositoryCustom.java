package com.hobbyhop.domain.post.repository.custom;

import com.hobbyhop.domain.post.dto.PostPageRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostPageResponseDTO> findAllByClubId(PostPageRequestDTO pageRequestDTO, Long clubId);

    void deleteAllElement(Long postId);
}

