package com.pratyush.RateShield.limiters.strategy;

import com.pratyush.RateShield.config.RateLimitConfig;
import com.pratyush.RateShield.limiters.IRateLimiter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("TOKEN_BUCKET")
public class TokenBucket implements IRateLimiter {
    private RedisTemplate<String, String> redisTemplate;

    public TokenBucket(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String key, RateLimitConfig rateLimitConfig) {
        String tokenKey = key + ":tokens";
        String lastRefillKey = key + ":lastrefill";
        long now = System.currentTimeMillis();

        // Fetch values
        String tokenStr = redisTemplate.opsForValue().get(tokenKey);
        String lastRefillTimeStr = redisTemplate.opsForValue().get(lastRefillKey);

        long tokens = tokenStr == null ? rateLimitConfig.getBurstCapacity() : Long.parseLong(tokenStr);
        long lastRefillTime = lastRefillTimeStr == null ? now : Long.parseLong(lastRefillTimeStr);

        // Refill logic
        long elapsed = (now - lastRefillTime) / 60000L;
        double tokensToAdd = elapsed * rateLimitConfig.getRefillRatePerMin();
        tokens = (long) Math.min(
                rateLimitConfig.getBurstCapacity(),
                tokens + tokensToAdd
        );

        if(tokens <= 0) {
            return false;
        }
        tokens--;

        redisTemplate.opsForValue().set(tokenKey, String.valueOf(tokens));
        redisTemplate.opsForValue().set(lastRefillKey, String.valueOf(now));

        return true;
    }
}
