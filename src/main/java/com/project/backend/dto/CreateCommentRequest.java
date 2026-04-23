package com.project.backend.dto;

import com.project.backend.entity.AuthorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCommentRequest {

    @NotNull
    private Long authorId;

    @NotNull
    private AuthorType authorType;

    @NotBlank
    private String content;

    private int depthLevel;

    // getters & setters
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public AuthorType getAuthorType() { return authorType; }
    public void setAuthorType(AuthorType authorType) { this.authorType = authorType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getDepthLevel() { return depthLevel; }
    public void setDepthLevel(int depthLevel) { this.depthLevel = depthLevel; }
}