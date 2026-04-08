package com.tugas.interceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import com.tugas.config.RateLimiterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Interceptor untuk Rate Limiting
 * Membatasi jumlah request dari setiap IP address
 */
@Component
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiterConfig rateLimiterConfig;

    private static final String X_RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String X_RATE_LIMIT_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip rate limiting untuk endpoints tertentu
        String requestUri = request.getRequestURI();
        if (isExcluded(requestUri)) {
            return true;
        }

        String clientIP = getClientIP(request);
        Bucket bucket = rateLimiterConfig.resolveBucket(clientIP);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Token tersedia, request diizinkan
            response.addHeader(X_RATE_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            // Token tidak tersedia, request ditolak
            // Estimate wait time (60 seconds for 100 requests per minute)
            long estimatedWaitSeconds = 60;
            response.addHeader(X_RATE_LIMIT_RETRY_AFTER, String.valueOf(estimatedWaitSeconds));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please retry after " + estimatedWaitSeconds + " seconds");

            log.warn("Rate limit exceeded for IP: {} ({})", clientIP, requestUri);
            return false;
        }
    }

    /**
     * Get client IP address dari request
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    /**
     * Check apakah endpoint diexklusikan dari rate limiting
     */
    private boolean isExcluded(String uri) {
        // Exclude health check, swagger, dan actuator endpoints
        return uri.matches("^/(swagger-ui|v3/api-docs|health|actuator).*");
    }
}
