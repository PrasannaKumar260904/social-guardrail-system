package com.project.backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisGuardService {

    private final StringRedisTemplate redisTemplate;

    public RedisGuardService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // STEP 4 — BOT LIMIT (ATOMIC)
    public boolean canBotReply(Long postId) {

        String key = "post:" + postId + ":bot_count";

        String current = redisTemplate.opsForValue().get(key);
        long count = (current != null) ? Long.parseLong(current) : 0;

        if (count >= 100) {
            return false; //  do NOT increment
        }

        redisTemplate.opsForValue().increment(key); //  increment only if allowed
        return true;
    }
    // STEP 5 — COOLDOWN (TTL)
    public boolean checkCooldown(Long botId, Long humanId) {

        String key = "cooldown:bot:" + botId + ":human:" + humanId;

        Boolean exists = redisTemplate.hasKey(key);

        if (Boolean.TRUE.equals(exists)) {
            return false;
        }

        redisTemplate.opsForValue()
                .set(key, "1", Duration.ofMinutes(10));

        return true;
    }
}