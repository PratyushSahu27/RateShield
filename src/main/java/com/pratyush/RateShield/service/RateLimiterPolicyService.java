package com.pratyush.RateShield.service;

import com.pratyush.RateShield.models.RateLimitPolicy;
import com.pratyush.RateShield.repository.PolicyRepository;
import com.pratyush.RateShield.types.ClientTypes;
import com.pratyush.RateShield.types.RateLimiterTypes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service to manage rate limit policies
 */
@Service
@RequiredArgsConstructor
public class RateLimiterPolicyService {
    private final Logger LOGGER = LoggerFactory.getLogger(RateLimiterPolicyService.class);
    private final PolicyRepository repository;

    /**
     * Fetches the rate limit policy for a given client ID. If no specific policy is found, returns a default policy.
     * @param clientId The client ID for which to fetch the rate limit policy
     * @return  The rate limit policy for the given client ID, or a default policy if none is found
     */
    public RateLimitPolicy getPolicy(ClientTypes clientType) {

        RateLimitPolicy policy =
                repository.get(String.valueOf(clientType));

        if(policy != null) {
            return policy;
        }

        LOGGER.info("No specific policy found for clientType: {}, returning default policy", clientType);
        return defaultPolicy();
    }

    /**
     * Saves or updates the rate limit policy for a given client ID
     * @param policy The rate limit policy to save or update
     */
    public void setPolicy(RateLimitPolicy policy) {
        repository.save(policy);
    }

    /**
     * Provides a default rate limit policy for free users using the token bucket algorithm
     * @return A default rate limit policy
     */
    private RateLimitPolicy defaultPolicy() {
        return new RateLimitPolicy()
                .builder()
                .clientRole(ClientTypes.FREE_USER)
                .algorithm(RateLimiterTypes.TOKEN_BUCKET)
                .burstCapacity(100l)
                .refillRatePerMin(50d)
                .limit(10l)
                .windowMs(60000l)
                .build();
    }
}
