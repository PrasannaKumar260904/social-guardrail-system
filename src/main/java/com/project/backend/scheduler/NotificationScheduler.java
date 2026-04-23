package com.project.backend.scheduler;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NotificationScheduler {

    private final StringRedisTemplate redisTemplate;

    public NotificationScheduler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void processNotifications() {

        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {

            Long size = redisTemplate.opsForList().size(key);

            if (size == null || size == 0) continue;

            // extract userId
            String userId = key.split(":")[1];

            System.out.println(
                    "📩 Summarized Notification → User " + userId +
                            ": Bot interactions (" + size + " times)"
            );

            // clear list
            redisTemplate.delete(key);
        }
    }
}