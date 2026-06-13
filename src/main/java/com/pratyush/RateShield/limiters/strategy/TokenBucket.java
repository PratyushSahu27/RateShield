package com.pratyush.RateShield.limiters.strategy;

import com.pratyush.RateShield.limiters.IRateLimiter;
import com.pratyush.RateShield.models.RateLimitPolicy;
import com.pratyush.RateShield.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("TOKEN_BUCKET")
public class TokenBucket implements IRateLimiter {
    private RedisRepository redisRepository;
    private Logger LOGGER = LoggerFactory.getLogger(TokenBucket.class);

    public TokenBucket(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Override
    public boolean isAllowed(String key, RateLimitPolicy policy) {
        LOGGER.info("TokenBucket: isAllowed called for key: {}, rateLimitConfig: {}", key, policy.getBurstCapacity());
        return redisRepository.tokenBucketScript(key, key + ":lastRefill", policy);
    }
}
