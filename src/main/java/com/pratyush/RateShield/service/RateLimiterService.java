package com.pratyush.RateShield.service;

import com.pratyush.RateShield.limiters.IRateLimiter;
import com.pratyush.RateShield.limiters.RateLimiterFactory;
import com.pratyush.RateShield.models.RateLimitEvent;
import com.pratyush.RateShield.models.RateLimitPolicy;
import com.pratyush.RateShield.types.ClientTypes;
import com.pratyush.RateShield.types.RateLimiterTypes;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private final RateLimiterFactory rateLimiterFactory;
    private final RateLimiterPolicyService rateLimiterPolicyService;
    private final RateLimitEventProducer rateLimitEventProducer;

    public RateLimiterService(RateLimiterFactory rateLimiterFactory, RateLimiterPolicyService rateLimiterPolicyService, RateLimitEventProducer rateLimitEventProducer) {
        this.rateLimiterFactory = rateLimiterFactory;
        this.rateLimiterPolicyService = rateLimiterPolicyService;
        this.rateLimitEventProducer = rateLimitEventProducer;
    }

    /**
     * This method checks if a user is allowed to access a specific API based on the rate limiting policy.
     *
     * @param api The API endpoint being accessed.
     * @param userId The ID of the user making the request.
     * @param clientType The type of client making the request (e.g., WEB, MOBILE).
     * @return true if the user is allowed to access the API, false otherwise.
     */
    public boolean isAllowed(String api, String userId, ClientTypes clientType) {
        RateLimitPolicy config = rateLimiterPolicyService.getPolicy(clientType);

        IRateLimiter rateLimiter = rateLimiterFactory.getRateLimiterStrategy(config.getAlgorithm());
        String key = buildKey(api, userId);

        boolean isAllowed = rateLimiter.isAllowed(key, config);
        RateLimitEvent event = RateLimitEvent.builder()
                .clientId(userId)
                .clientRole(clientType.name())
                .algorithm(String.valueOf(config.getAlgorithm()))
                .endpoint(api)
                .allowed(isAllowed)
                .timestamp(System.currentTimeMillis())
                .build();
        rateLimitEventProducer.publishEvent(event);

        return rateLimiter.isAllowed(key, config);
    }

    /**
     * This method builds a unique key for rate limiting based on the API endpoint and user ID.
     *
     * @param api The API endpoint being accessed.
     * @param userId The ID of the user making the request.
     * @return A unique key for rate limiting.
     */
    private String buildKey(String api, String userId) {
        return "rate:" + api + ":user:" + userId;
    }

    /**
     * This method determines the rate limiting strategy to use based on the API endpoint.
     *
     * @param api The API endpoint being accessed.
     * @return The rate limiting strategy to use for the given API endpoint.
     */
    private RateLimiterTypes getRateLimitType(String api) {
        RateLimiterTypes type = RateLimiterTypes.LEAKY_BUCKET;

        if(api.contains("user")) {
            type = RateLimiterTypes.TOKEN_BUCKET;
        }

        if(api.contains("profile")) {
            type = RateLimiterTypes.FIXED_WINDOW;
        }

        return type;
    }
}
