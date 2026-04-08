package com.tugas.service;

import com.tugas.dto.LoginRequest;
import com.tugas.dto.LoginResponse;
import com.tugas.dto.RegisterRequest;
import com.tugas.dto.RegisterResponse;
import com.tugas.entity.Role;
import com.tugas.entity.User;
import com.tugas.exception.BadApiRequest;
import com.tugas.exception.ResourceNotFoundException;
import com.tugas.repository.RoleRepository;
import com.tugas.repository.UserRepository;
import com.tugas.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordValidator passwordValidator;

    /**
     * Register user baru
     */
    public RegisterResponse register(RegisterRequest request) {
        registerUserInternal(request);
        return registerUser(request);
    }

    /**
     * Register user baru (untuk testing dan internal use)
     */
    public RegisterResponse registerUser(RegisterRequest request) {
        log.info("Registering user: {}", request.getUsername());

        // Validasi input
        validateRegisterRequest(request);

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set default role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));
        user.getRoles().add(userRole);

        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        return new RegisterResponse(
                true,
                "User berhasil didaftarkan",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    /**
     * Validasi register request
     */
    private void validateRegisterRequest(RegisterRequest request) {
        // Validasi username
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new BadApiRequest("Username tidak boleh kosong");
        }

        // Validasi email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BadApiRequest("Email tidak boleh kosong");
        }

        // Validasi format email
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadApiRequest("Format email tidak valid");
        }

        // Validasi password strength
        String passwordError = passwordValidator.getPasswordValidationError(request.getPassword());
        if (passwordError != null) {
            throw new BadApiRequest(passwordError);
        }

        // Cek apakah username sudah ada
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadApiRequest("Username sudah digunakan");
        }

        // Cek apakah email sudah ada
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadApiRequest("Email sudah digunakan");
        }
    }

    /**
     * Register user dengan validation (original logic)
     */
    private void registerUserInternal(RegisterRequest request) {
        log.info("Registering user: {}", request.getUsername());

        // Validasi password dan confirm password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadApiRequest("Password dan Confirm Password tidak cocok");
        }

        // Cek apakah username sudah ada
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadApiRequest("Username sudah digunakan");
        }

        // Cek apakah email sudah ada
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadApiRequest("Email sudah digunakan");
        }
    }

    /**
     * Login user
     */
    public LoginResponse login(LoginRequest request) {
        log.info("Logging in user: {}", request.getUsername());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get user dari database
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.getUsername()));

            // Generate JWT token
            String token = tokenProvider.generateToken(authentication);

            // Get roles
            Set<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            log.info("User logged in successfully: {}", user.getUsername());

            return new LoginResponse(
                    token,
                    "Bearer",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    roles
            );

        } catch (Exception ex) {
            log.error("Login failed for user: {}", request.getUsername());
            throw new BadApiRequest("Username atau Password salah");
        }
    }

    /**
     * Get user profile
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Check apakah username exist
     */
    public Boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check apakah username exist (alias)
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check apakah email exist
     */
    public Boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check apakah email exist (alias)
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
