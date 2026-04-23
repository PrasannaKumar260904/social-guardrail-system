package com.project.backend.dto;

public class PostResponseDto {

    private Long id;
    private String content;
    private Long authorId;
    private String authorType;
    private int likeCount;

    public PostResponseDto(Long id,
                           String content,
                           Long authorId,
                           String authorType,
                           int likeCount) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorType = authorType;
        this.likeCount = likeCount;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public Long getAuthorId() { return authorId; }
    public String getAuthorType() { return authorType; }
    public int getLikeCount() { return likeCount; }
}