package com.pratyush.RateShield.repository;

import com.pratyush.RateShield.models.RateLimitPolicy;
import com.pratyush.RateShield.types.ClientTypes;
import com.pratyush.RateShield.types.RateLimiterTypes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Repository for managing rate limit policies in Redis
 */
@Repository
@RequiredArgsConstructor
public class PolicyRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(PolicyRepository.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(RateLimitPolicy policy) {

        String key = "policy:" + policy.getClientRole();

        redisTemplate.opsForHash().put(key,
                "algorithm",
                policy.getAlgorithm().name());

        redisTemplate.opsForHash().put(key,
                "burstCapacity",
                policy.getBurstCapacity());

        redisTemplate.opsForHash().put(key,
                "refillRatePerMin",
                policy.getRefillRatePerMin());

        redisTemplate.opsForHash().put(key,
                "limit",
                policy.getLimit());

        redisTemplate.opsForHash().put(key,
                "windowMs",
                policy.getWindowMs());
        LOGGER.info("Saved policy for clientType: {}", policy.getClientRole());
    }

    public RateLimitPolicy get(String clientType) {

        String key = "policy:" + clientType;

        Map<Object,Object> values = redisTemplate.opsForHash().entries(key);

        if(values.isEmpty()) {
            return null;
        }

        return RateLimitPolicy.builder()
                .clientRole(ClientTypes.valueOf(clientType))
                .algorithm(
                        RateLimiterTypes.valueOf(
                                (String) values.get("algorithm")
                        )
                )
                .limit((Long) values.get("limit"))
                .windowMs((Long) values.get("windowMs"))
                .burstCapacity((Long) values.get("burstCapacity") )
                .refillRatePerMin((Double) values.get("refillRatePerMin"))
                .build();
    }
}