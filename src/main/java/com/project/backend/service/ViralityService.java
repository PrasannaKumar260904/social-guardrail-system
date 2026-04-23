package com.project.backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViralityService {

    private final StringRedisTemplate redisTemplate;

    public ViralityService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void increaseScore(Long postId, int points) {
        String key = "post:" + postId + ":virality_score";
        redisTemplate.opsForValue().increment(key, points);
    }

    public Long getScore(Long postId) {
        String key = "post:" + postId + ":virality_score";
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }
}