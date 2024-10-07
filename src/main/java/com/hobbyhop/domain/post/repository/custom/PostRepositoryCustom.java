package com.hobbyhop.domain.post.repository.custom;

import com.hobbyhop.domain.post.dto.PostPageRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import org.springframework.data.domain.Page;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PostRepositoryCustom {
    Page<PostPageResponseDTO> findAllByClubId(PostPageRequestDTO pageRequestDTO, Long clubId);

    Optional<Post> findByIdWithImages(Long clubId, Long postId);

    void deleteAllElement(Long postId);
}

