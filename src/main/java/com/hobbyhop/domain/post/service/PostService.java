package com.hobbyhop.domain.post.service;

import com.hobbyhop.domain.post.dto.PostModifyRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.PageResponseDTO;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {

    Post findPost(Long postId);

    PostResponseDTO makePost(User user, Long clubId, PostRequestDTO postRequestDTO);

    void imageUploadPost(User user, Long clubId, Long postId, MultipartFile file) throws IOException;

    PostResponseDTO getPostById(User user, Long clubId, Long postId);

    PageResponseDTO<PostPageResponseDTO> getAllPost(PageRequestDTO pageRequestDTO, Long clubId);

    PostResponseDTO modifyPost(User user, Long clubId, Long postId,
            MultipartFile file, PostModifyRequestDTO postModifyRequestDTO)
            throws IOException;

    void deletePost(User user, Long clubId, Long postId);

    void makePostUser(User user, Long clubId, Long postId);


}
