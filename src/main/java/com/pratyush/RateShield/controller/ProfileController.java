package com.pratyush.RateShield.controller;

import com.pratyush.RateShield.service.RateLimiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final RateLimiterService rateLimiterService;

    public ProfileController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }
    @RequestMapping("/info")
    public ResponseEntity<String> getProfileInfo(@RequestParam String userId) {
        if(!rateLimiterService.isAllowed("profile", userId)) {
            return ResponseEntity.status(429).body("Too many requests");
        }
        return ResponseEntity.ok("This is the profile info");
    }
}
