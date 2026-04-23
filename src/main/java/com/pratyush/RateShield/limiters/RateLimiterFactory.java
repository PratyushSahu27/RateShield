package com.pratyush.RateShield.limiters;

import com.pratyush.RateShield.types.RateLimiterTypes;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RateLimiterFactory {

    private final Map<String, IRateLimiter> strategyMap;

    public RateLimiterFactory(Map<String, IRateLimiter> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public IRateLimiter getRateLimiterStrategy(RateLimiterTypes type) {
        IRateLimiter strategy = strategyMap.get(type.name());

        if (strategy == null) {
            throw new RuntimeException("No strategy found for " + type);
        }

        return strategy;
    }
}
