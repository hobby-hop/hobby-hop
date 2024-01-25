package com.hobbyhop.domain.post.repository.custom;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostResponseDTO> findAllByClubId(PageRequestDTO pageRequestDTO, Long clubId);

    void deleteAllElement(Long postId);
}
