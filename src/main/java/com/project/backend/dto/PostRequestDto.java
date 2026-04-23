package com.project.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostRequestDto {

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotBlank(message = "Author type is required")
    private String authorType; // must be "USER" or "BOT"

    @NotBlank(message = "Content cannot be empty")
    private String content;

    // getters & setters
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getAuthorType() { return authorType; }
    public void setAuthorType(String authorType) {
        this.authorType = authorType != null ? authorType.trim().toUpperCase() : null;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}