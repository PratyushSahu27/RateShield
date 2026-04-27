package com.pratyush.RateShield.controller;

import com.pratyush.RateShield.service.RateLimiterService;
import com.pratyush.RateShield.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final RateLimiterService rateLimiterService;
    private final String getUserApi = "getuser";

    public UserController(UserService userService, RateLimiterService rateLimiterService) {
        this.userService = userService;
        this.rateLimiterService = rateLimiterService;
    }


    @GetMapping("/" + getUserApi)
    public ResponseEntity<String> getUser(@RequestParam String userId) {
        if(!rateLimiterService.isAllowed(getUserApi, userId)) {
            return ResponseEntity.status(429).body("Too many requests");
        }

        return ResponseEntity.ok(userService.getUser());
    }
}
