package com.pratyush.RateShield.limiters;

import com.pratyush.RateShield.models.RateLimitPolicy;

public interface IRateLimiter {
    public boolean isAllowed(String key, RateLimitPolicy rateLimitPolicy);
}
