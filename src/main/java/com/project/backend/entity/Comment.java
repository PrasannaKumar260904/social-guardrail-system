
package com.project.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SIMPLE & SCALABLE (NO RELATION)
    private Long postId;

    private Long authorId;

    @Enumerated(EnumType.STRING)
    private AuthorType authorType;

    private String content;

    private int depthLevel;

    private LocalDateTime createdAt = LocalDateTime.now();
}