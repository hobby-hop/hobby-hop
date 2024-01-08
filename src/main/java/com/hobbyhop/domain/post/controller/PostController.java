package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs/{clubId}")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public PostResponseDTO makePost(@PathVariable Long clubId,
            @RequestBody @Valid PostRequestDTO postRequestDTO){
            //@AuthenticationPrincipal UserDetailsImpl userDetails){

        return postService.makePost(clubId, postRequestDTO);
    }

    @GetMapping("/posts/{postId}")
    public PostResponseDTO getPostById(@PathVariable Long clubId, @PathVariable Long postId) {

        return postService.getPostById(clubId, postId);
    }

    @GetMapping("/posts")
    public List<PostResponseDTO> getAllPost(@PathVariable Long clubId) {

        return postService.getAllPost(clubId);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponseDTO modifyPost(@PathVariable Long clubId, @PathVariable Long postId,
            @RequestBody @Valid PostRequestDTO postRequestDTO) {

        return postService.modifyPost(clubId, postId, postRequestDTO);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long clubId, @PathVariable Long postId) {

        postService.deletePost(clubId, postId);
    }
}
