package com.pratyush.RateShield.service;

import com.pratyush.RateShield.config.RateLimitConfig;
import com.pratyush.RateShield.limiters.IRateLimiter;
import com.pratyush.RateShield.limiters.RateLimiterFactory;
import com.pratyush.RateShield.types.RateLimiterTypes;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private final RateLimiterFactory rateLimiterFactory;

    public RateLimiterService(RateLimiterFactory rateLimiterFactory) {
        this.rateLimiterFactory = rateLimiterFactory;
    }

    public boolean isAllowed(String api, String userId) {
        RateLimiterTypes type = getRateLimitType(api);

        IRateLimiter rateLimiter = rateLimiterFactory.getRateLimiterStrategy(type);
        String key = buildKey(api, userId);
        RateLimitConfig config = new RateLimitConfig(10, 60000, 5, 5);

        return rateLimiter.isAllowed(key, config);
    }

    private String buildKey(String api, String userId) {
        return "rate:" + api + ":user:" + userId;
    }

    private RateLimiterTypes getRateLimitType(String api) {
        RateLimiterTypes type = RateLimiterTypes.LEAKY_BUCKET;

        if(api.contains("user")) {
            type = RateLimiterTypes.FIXED_BUCKET;
        }

        return type;
    }
}
