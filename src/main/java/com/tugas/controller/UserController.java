package com.tugas.controller;

import com.tugas.dto.ApiResponse;
import com.tugas.entity.User;
import com.tugas.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UserController {

    @Autowired
    private AuthService authService;

    /**
     * Get current user profile (memerlukan authentication)
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getCurrentUser(Authentication authentication) {
        log.info("Get current user: {}", authentication.getName());
        User user = authService.getUserByUsername(authentication.getName());
        ApiResponse response = new ApiResponse(true, "User profile berhasil diambil", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Test endpoint yang hanya bisa diakses oleh authenticated user
     */
    @GetMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> testEndpoint() {
        log.info("Test endpoint");
        ApiResponse response = new ApiResponse(true, "Endpoint ini hanya bisa diakses oleh authenticated user");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Public endpoint (tidak memerlukan authentication)
     */
    @GetMapping("/public/info")
    public ResponseEntity<ApiResponse> getPublicInfo() {
        log.info("Get public info");
        ApiResponse response = new ApiResponse(true, "Ini adalah public endpoint");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
