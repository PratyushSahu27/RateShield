package com.pratyush.RateShield.limiters.strategy;

import com.pratyush.RateShield.config.RateLimitConfig;
import com.pratyush.RateShield.limiters.IRateLimiter;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;

public class FixedWindow implements IRateLimiter {
    private final RedisTemplate<String, String> redisTemplate;

    public FixedWindow(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String key, RateLimitConfig rateLimitConfig) {
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1) {
            redisTemplate.expire(key, Duration.ofMillis(rateLimitConfig.getWindowMs()));
        }

        return count < rateLimitConfig.getLimit();
    }
}
