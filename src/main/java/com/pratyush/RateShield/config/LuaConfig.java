package com.pratyush.RateShield.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class LuaConfig {

    /**
     * Bean of the fixed window lua script
     * @return
     */
    @Bean
    public RedisScript<Long> fixedWindowScript(){
        return RedisScript.of(
                new ClassPathResource("scripts/fixed-window.lua"),
                Long.class
        );
    }

    /**
     * Bean of token bucket lua script
     * @return
     */
    @Bean
    public RedisScript<Long> tokenBucketScript() {
        return RedisScript.of(
                new ClassPathResource("scripts/token-bucket.lua"),
                Long.class
        );
    }
 }
