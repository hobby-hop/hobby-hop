package com.hobbyhop.domain.post.repository.custom;

import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.global.request.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface PostRepositoryCustom {

    Page<PostResponseDTO> findAllByClubId(PageRequestDTO pageRequestDTO, Long clubId);

    void deleteAllElement(Long postId);
}
