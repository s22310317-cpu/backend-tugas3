package com.tugas.config;

import com.tugas.interceptor.RateLimitingInterceptor;
import com.tugas.interceptor.RequestResponseLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration
 * Mengatur interceptors dan web configurations
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitingInterceptor rateLimitingInterceptor;

    @Autowired
    private RequestResponseLoggingInterceptor requestResponseLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register request/response logging interceptor
        registry.addInterceptor(requestResponseLoggingInterceptor)
                .addPathPatterns("/**");

        // Register rate limiting interceptor
        registry.addInterceptor(rateLimitingInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/register", "/api/auth/login", "/api/users/public/**");
    }
}
