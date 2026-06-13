package com.pratyush.RateShield.limiters.strategy;

import com.pratyush.RateShield.limiters.IRateLimiter;
import com.pratyush.RateShield.models.RateLimitPolicy;
import com.pratyush.RateShield.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("FIXED_WINDOW")
public class FixedWindow implements IRateLimiter {
    private final RedisRepository repository;
    private Logger LOGGER = LoggerFactory.getLogger(TokenBucket.class);

    public FixedWindow(RedisRepository redisRepository) {
        this.repository = redisRepository;
    }

    @Override
    public boolean isAllowed(String key, RateLimitPolicy rateLimitPolicy) {
        LOGGER.info("FixedWindow: isAllowed called for key: {}, rateLimit: {}", key, rateLimitPolicy.getLimit());
        return repository.incrementAndGet(key, rateLimitPolicy);
    }
}
