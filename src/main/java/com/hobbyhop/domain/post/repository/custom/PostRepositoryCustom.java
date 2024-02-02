package com.hobbyhop.domain.post.repository.custom;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.global.request.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostPageResponseDTO> findAllByClubId(Pageable pageable, Long clubId, String keyword);

    void deleteAllElement(Long postId);

    Page<PostResponseDTO> findAllByClubIdAndKeyword(PageRequestDTO pageRequestDTO, Long clubId);
}

