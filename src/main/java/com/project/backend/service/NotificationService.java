package com.project.backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NotificationService {

    private final StringRedisTemplate redisTemplate;

    public NotificationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void handleBotNotification(Long userId, String message) {

        String cooldownKey = "user:" + userId + ":notif_cooldown";

        Boolean exists = redisTemplate.hasKey(cooldownKey);

        if (Boolean.TRUE.equals(exists)) {
            //  push into list
            String listKey = "user:" + userId + ":pending_notifs";
            redisTemplate.opsForList().rightPush(listKey, message);
        } else {
            // send immediately (simulate)
            System.out.println(" Push Notification Sent to User " + userId + ": " + message);

            // set cooldown (15 mins)
            redisTemplate.opsForValue().set(
                    cooldownKey,
                    "1",
                    Duration.ofMinutes(15)
            );
        }
    }
}