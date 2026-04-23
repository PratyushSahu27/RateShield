package com.pratyush.RateShield.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    String name;

    public UserService() {
        this.name = "Pratyush";
    }

    public String getUser() {
        return this.name;
    }
}
