package com.pratyush.RateShield.service;

import com.pratyush.RateShield.models.RateLimitEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RateLimitEventProducer {
    private final KafkaTemplate<String, RateLimitEvent> kafkaTemplate;
    private final String topic = "rate-limit-events";

    public void publishEvent(RateLimitEvent event) {
        kafkaTemplate.send(topic, event.clientId(), event);
    }
}
