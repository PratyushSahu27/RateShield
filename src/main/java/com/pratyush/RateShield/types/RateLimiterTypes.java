package com.pratyush.RateShield.types;

/**
 * Enum representing different types of rate limiters that can be used in the system.
 * Each type corresponds to a specific algorithm for controlling the rate of requests.
 */
public enum RateLimiterTypes {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW
}
