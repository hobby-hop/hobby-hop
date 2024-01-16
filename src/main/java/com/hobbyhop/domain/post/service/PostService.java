package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.PostRepository;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {

    Post findPost(Long postId);

    PostResponseDTO makePost(UserDetailsImpl userDetails, Long clubId, MultipartFile file, PostRequestDTO postRequestDTO)
            throws IOException;

    PostResponseDTO getPostById(Long clubId, Long postId);

    PostPageResponseDTO getAllPost(Pageable pageable, Long clubId);

    PostResponseDTO modifyPost(UserDetailsImpl userDetails, Long clubId, Long postId, PostRequestDTO postRequestDTO);

    void deletePost(UserDetailsImpl userDetails, Long clubId, Long postId);

    void makePostUser(UserDetailsImpl userDetails, Long clubId, Long postId);
}
