package com.pratyush.RateShield.models;

import com.pratyush.RateShield.types.ClientTypes;
import com.pratyush.RateShield.types.RateLimiterTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimitPolicy {
    private ClientTypes clientRole; // e.g., "FREE_USER", "PREMIUM_USER", "ADMIN"
    private RateLimiterTypes algorithm;

    // Fixed Window
    private Long limit;
    private Long windowMs;

    // Token Bucket
    private Long burstCapacity;
    private Double refillRatePerMin;
}
