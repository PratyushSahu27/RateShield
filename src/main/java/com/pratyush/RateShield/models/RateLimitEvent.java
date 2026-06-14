package com.pratyush.RateShield.models;

import lombok.Builder;

/**
 * This record represents an event that occurs when a rate limit check is performed.
 * It captures details about the request, the client, the endpoint, the algorithm used,
 * whether the request was allowed or blocked, and the remaining tokens (if applicable).
 */
@Builder
public record RateLimitEvent(
        String requestId,
        String clientId,
        String clientRole,
        String endpoint,
        String algorithm,
        boolean allowed,
        long timestamp,
        long remainingTokens
) {
}
