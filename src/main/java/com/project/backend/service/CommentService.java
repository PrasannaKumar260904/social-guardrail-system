package com.project.backend.service;

import com.project.backend.dto.CreateCommentRequest;
import com.project.backend.entity.AuthorType;
import com.project.backend.entity.Comment;
import com.project.backend.entity.Post;
import com.project.backend.exception.BadRequestException;
import com.project.backend.exception.BotLimitExceededException;
import com.project.backend.exception.CooldownException;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.repository.CommentRepository;
import com.project.backend.repository.PostRepository;

import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final RedisGuardService redisGuardService;
    private final ViralityService viralityService;
    private final NotificationService notificationService;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          RedisGuardService redisGuardService,
                          ViralityService viralityService, NotificationService notificationService) {

        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.redisGuardService = redisGuardService;
        this.viralityService = viralityService;
        this.notificationService = notificationService;
    }

    public Comment addComment(Long postId, CreateCommentRequest request) {

        Post post = postRepository.findById(postId)
                
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (request.getDepthLevel() > 20) {

            throw new BadRequestException("Max depth exceeded (20)");
        }

        // 🔥 BOT RULES
        if (request.getAuthorType() == AuthorType.BOT) {

            // ✅ BOT LIMIT (use service)
            if (!redisGuardService.canBotReply(postId)) {
                throw new BotLimitExceededException("Bot limit exceeded (100 max)");
                
            }

            // ✅ COOLDOWN (use service)
            if (!redisGuardService.checkCooldown(
                    request.getAuthorId(),
                    post.getAuthorId()
            )) {
                
                throw new CooldownException("Cooldown active (10 mins)");
            }

            // ✅ VIRALITY
            viralityService.increaseScore(postId, 1);
            notificationService.handleBotNotification(
                    post.getAuthorId(),
                    "Bot " + request.getAuthorId() + " replied to your post"
            );

        } else {
            // 👤 HUMAN COMMENT
            viralityService.increaseScore(postId, 50);
        }

        Comment comment = new Comment();
        comment.setPostId(postId); // ✅ IMPORTANT (your entity uses postId, not Post object)
        comment.setAuthorId(request.getAuthorId());
        comment.setAuthorType(request.getAuthorType());
        comment.setContent(request.getContent());
        comment.setDepthLevel(request.getDepthLevel());

        return commentRepository.save(comment);
    }
}