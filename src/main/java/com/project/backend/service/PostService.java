package com.project.backend.service;

import com.project.backend.dto.*;
import com.project.backend.entity.AuthorType;
import com.project.backend.entity.Post;
import com.project.backend.repository.PostRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ViralityService viralityService;

    public PostService(PostRepository postRepository, ViralityService viralityService) {
        this.postRepository = postRepository;
        this.viralityService = viralityService;
    }

    //  CREATE POST
    public PostResponseDto createPost(PostRequestDto dto) {

        Post post = new Post();

        post.setAuthorId(dto.getAuthorId());

        //  SAFE ENUM CONVERSION
        try {
            post.setAuthorType(AuthorType.valueOf(dto.getAuthorType().toUpperCase()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid authorType. Use USER or BOT");
        }

        post.setContent(dto.getContent());
        post.setLikeCount(0); // explicit

        Post saved = postRepository.save(post);

        return mapToDto(saved);
    }

    //  GET POSTS (for now all posts — later filter by user/bot)
    public PaginationResponse<PostResponseDto> getMyPosts(Pageable pageable) {

        Page<Post> page = postRepository.findAll(pageable);

        List<PostResponseDto> posts = page.getContent()
                .stream()
                .map(this::mapToDto)
                .toList();

        return new PaginationResponse<>(
                posts,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    //  UPDATE POST
    public PostResponseDto updatePost(Long postId, PostRequestDto dto) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setContent(dto.getContent());

        Post updated = postRepository.save(post);

        return mapToDto(updated);
    }

    //  DELETE POST
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postRepository.delete(post);
    }
    //like post
    public void likePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        //  VIRALITY
        viralityService.increaseScore(postId, 20);
    }

    //  CENTRALIZED DTO MAPPING (VERY IMPORTANT)
    private PostResponseDto mapToDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getContent(),
                post.getAuthorId(),
                post.getAuthorType().name(),
                post.getLikeCount()
        );
    }
}