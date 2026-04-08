package com.tugas.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Interceptor untuk logging request dan response
 * Mencatat semua incoming requests dan outgoing responses
 */
@Component
@Slf4j
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "X-Request-ID";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Generate unique request ID
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID, requestId);

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        // Log request details
        log.info("==> REQUEST [ID: {}] {} {}", 
                requestId, 
                request.getMethod(), 
                request.getRequestURI());
        
        log.debug("Request Headers: {}", getHeadersAsString(request));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestId = (String) request.getAttribute(REQUEST_ID);
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        // Log response details
        log.info("<== RESPONSE [ID: {}] Status: {} Duration: {} ms", 
                requestId, 
                response.getStatus(), 
                duration);

        if (ex != null) {
            log.error("Exception occurred during request processing", ex);
        }
    }

    /**
     * Helper method untuk menampilkan headers dalam format string
     */
    private String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        var headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            
            // Jangan log sensitive headers
            if (isSensitiveHeader(headerName)) {
                headerValue = "***MASKED***";
            }
            
            headers.append(headerName).append(": ").append(headerValue).append(" ");
        }
        
        return headers.toString();
    }

    /**
     * Check jika header adalah sensitive dan perlu di-mask
     */
    private boolean isSensitiveHeader(String headerName) {
        return headerName.toLowerCase().contains("authorization") ||
               headerName.toLowerCase().contains("password") ||
               headerName.toLowerCase().contains("token") ||
               headerName.toLowerCase().contains("secret") ||
               headerName.toLowerCase().contains("api-key");
    }
}
