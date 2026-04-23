package com.project.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType; //  ENUM (safe)

    private String content;

    private int likeCount = 0;

    private LocalDateTime createdAt;

    // Automatically set before saving
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}