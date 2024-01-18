package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {

    Post findPost(Long postId);

    PostResponseDTO makePost(User user, Long clubId, PostRequestDTO postRequestDTO);

    void imageUploadPost(User user, Long clubId, Long postId, MultipartFile file) throws IOException;

    PostResponseDTO getPostById(Long clubId, Long postId);

    PostPageResponseDTO getAllPost(Pageable pageable, Long clubId);

    PostResponseDTO modifyPost(User user, Long clubId, Long postId,
            MultipartFile file, PostRequestDTO postRequestDTO)
            throws IOException;

    void deletePost(User user, Long clubId, Long postId);

    void makePostUser(User user, Long clubId, Long postId);


}
