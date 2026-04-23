package com.project.backend.controller;

import com.project.backend.dto.*;
import com.project.backend.entity.Comment;
import com.project.backend.service.CommentService;
import com.project.backend.service.PostService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService,
                          CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    //  CREATE POST (JWT user)
    @PostMapping("/posts")
    public PostResponseDto createPost(
            @Valid @RequestBody PostRequestDto dto) {

        return postService.createPost(dto);
    }

    // GET MY POSTS
    @GetMapping("/posts/my")
    public PaginationResponse<PostResponseDto> getMyPosts(Pageable pageable) {

        return postService.getMyPosts(pageable);
    }

    //  UPDATE POST
    @PutMapping("/posts/{postId}")
    public PostResponseDto updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto dto) {

        return postService.updatePost(postId, dto);
    }

    //  DELETE POST
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);
        return "Post deleted successfully";
    }

    // ADD COMMENT
    @PostMapping("/posts/{postId}/comments")
    public Comment addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request) {

        return commentService.addComment(postId, request);
    }

    // LIKE POST
    @PostMapping("/posts/{postId}/like")
    public String likePost(@PathVariable Long postId) {

        postService.likePost(postId);
        return "Post liked successfully";
    }
}