package com.tugas.controller;

import com.tugas.dto.*;
import com.tugas.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Register request: {}", registerRequest.getUsername());
        RegisterResponse registerResponse = authService.register(registerRequest);
        ApiResponse response = new ApiResponse(true, "Registrasi berhasil", registerResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest.getUsername());
        LoginResponse loginResponse = authService.login(loginRequest);
        ApiResponse response = new ApiResponse(true, "Login berhasil", loginResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Check username available
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<ApiResponse> checkUsername(@PathVariable String username) {
        log.info("Check username: {}", username);
        Boolean exists = authService.existsByUsername(username);
        ApiResponse response = new ApiResponse(!exists, exists ? "Username sudah digunakan" : "Username tersedia");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Check email available
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse> checkEmail(@PathVariable String email) {
        log.info("Check email: {}", email);
        Boolean exists = authService.existsByEmail(email);
        ApiResponse response = new ApiResponse(!exists, exists ? "Email sudah digunakan" : "Email tersedia");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
