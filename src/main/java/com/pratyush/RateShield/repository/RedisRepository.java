package com.pratyush.RateShield.repository;

import com.pratyush.RateShield.config.RateLimitConfig;
import com.pratyush.RateShield.limiters.strategy.TokenBucket;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Long>  fixedWindowScript;
    private final RedisScript<Long> tokenBucketScript;
    private Logger LOGGER = LoggerFactory.getLogger(RedisRepository.class);

    /**
     * Executes the fixed window lua script to increment the count and check if it exceeds the limit.
     * @param key
     * @param config
     * @return
     */
    public boolean incrementAndGet(String key, RateLimitConfig config) {
        long result = redisTemplate.execute(
                fixedWindowScript,
                Collections.singletonList(key),
                String.valueOf(config.getWindowMs()),
                String.valueOf(config.getLimit())
        );
        LOGGER.info("Lua result={}", result);
        return result == 1L;
    }

    /**
     * Executes the token bucket lua script to check if a token can be consumed and refills tokens based on the elapsed time.
     * @param tokenKey
     * @param lastRefillKey
     * @param config
     * @return
     */
    public boolean tokenBucketScript(String tokenKey, String lastRefillKey, RateLimitConfig config) {

            long result = redisTemplate.execute(
                    tokenBucketScript,
                    List.of(tokenKey, lastRefillKey),
                    String.valueOf(config.getBurstCapacity()),
                    String.valueOf(System.currentTimeMillis()),
                    String.valueOf(config.getRefillRatePerMin())
            );
            LOGGER.info("Lua result={}", result);
        return result == 1L;
    }
}
