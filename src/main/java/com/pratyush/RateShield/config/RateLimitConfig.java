package com.pratyush.RateShield.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RateLimitConfig {
    private int limit;
    private int windowMs;
    private int burstCapacity;
    private long refillRatePerMin;
}
