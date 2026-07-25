package com.paymentservice.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentIdempotencyService {

    private final StringRedisTemplate redisTemplate;

    public PaymentIdempotencyService( StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String PREFIX = "payment:processed:";
    private static final Duration TTL = Duration.ofHours(24);

    public boolean tryMarkProcessed(UUID orderID) {
        String key = PREFIX + orderID;

        Boolean firsTime = redisTemplate.opsForValue()
                .setIfAbsent(key, Instant.now().toString(), TTL);

        return Boolean.TRUE.equals(firsTime);
    }

}
