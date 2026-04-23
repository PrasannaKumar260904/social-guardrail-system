package com.project.backend.dto;

import lombok.Data;

@Data
public class CreatePostRequest {
    private Long authorId;
    private String authorType; // USER or BOT
    private String content;
}