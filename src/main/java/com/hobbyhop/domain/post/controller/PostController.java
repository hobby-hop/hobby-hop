package com.hobbyhop.domain.post.controller;

import com.hobbyhop.domain.post.dto.PostRequestDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public PostResponseDTO createBoard(@PathVariable Long clubId,
            @RequestBody @Valid PostRequestDTO postRequestDTO){
            //@AuthenticationPrincipal UserDetailsImpl userDetails){

        return postService.createBoard(clubId, postRequestDTO);
    }

    @GetMapping("/posts/{postId}")
    public PostResponseDTO getGameById(@PathVariable Long clubId, @PathVariable Long postId) {

        return postService.getPostById(clubId, postId);
    }

}
