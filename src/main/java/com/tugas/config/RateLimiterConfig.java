package com.tugas.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiter Configuration
 * Menggunakan Bucket4j untuk implement token bucket algorithm
 */
@Component
public class RateLimiterConfig {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Membuat bucket untuk rate limiting berdasarkan IP address
     * Default: 100 requests per 1 menit per IP
     */
    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }

    /**
     * Membuat bucket baru dengan konfigurasi default
     * 100 requests per 1 menit
     */
    private Bucket createNewBucket() {
        return Bucket4j.builder()
                .addLimit(io.github.bucket4j.Bandwidth.classic(100, io.github.bucket4j.Refill.intervally(100, Duration.ofMinutes(1))))
                .build();
    }

    /**
     * Reset bucket untuk IP tertentu (misalnya untuk testing)
     */
    public void resetBucket(String ip) {
        cache.remove(ip);
    }

    /**
     * Reset semua buckets
     */
    public void resetAll() {
        cache.clear();
    }
}
