package com.pratyush.RateShield.controller;

import com.pratyush.RateShield.models.RateLimitPolicy;
import com.pratyush.RateShield.service.RateLimiterPolicyService;
import com.pratyush.RateShield.types.ClientTypes;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policy")
public class RateLimitPolicyController {
    private final RateLimiterPolicyService rateLimiterPolicyService;

    public RateLimitPolicyController(RateLimiterPolicyService rateLimiterPolicyService) {
        this.rateLimiterPolicyService = rateLimiterPolicyService;
    }

    @PostMapping
    public String createPolicy(@RequestBody RateLimitPolicy rateLimitPolicy) {
        rateLimiterPolicyService.setPolicy(rateLimitPolicy);
        return "Policy created successfully";
    }

    @GetMapping
    public RateLimitPolicy getPolicy(@RequestParam String clientType) {
        // Implement logic to fetch the rate limit policy for the given client type
        return rateLimiterPolicyService.getPolicy(ClientTypes.valueOf(clientType));
    }
}
