package com.pratyush.RateShield.types;

/**
 * Enum representing different types of clients in the rate limiting system.
 * This can be used to categorize clients and apply different rate limits based on their type.
 */
public enum ClientTypes {
    FREE_USER,
    PREMIUM_USER,
    ADMIN,
    INTERNAL_SERVICE,
    PARTNER_API
}
