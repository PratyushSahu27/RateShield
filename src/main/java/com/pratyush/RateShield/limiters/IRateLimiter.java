package com.pratyush.RateShield.limiters;

import com.pratyush.RateShield.config.RateLimitConfig;

public interface IRateLimiter {
    public boolean isAllowed(String key, RateLimitConfig rateLimitConfig);
}
