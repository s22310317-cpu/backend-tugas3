package com.tugas.service;

import com.tugas.dto.RegisterRequest;
import com.tugas.dto.RegisterResponse;
import com.tugas.entity.Role;
import com.tugas.entity.User;
import com.tugas.exception.BadApiRequest;
import com.tugas.exception.ResourceNotFoundException;
import com.tugas.repository.RoleRepository;
import com.tugas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);
    }

    @Test
    void testRegisterUserSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("SecurePass123!");

        RegisterResponse response = authService.registerUser(request);

        assertNotNull(response);
        assertEquals("newuser", response.getUsername());
        assertEquals("newuser@example.com", response.getEmail());

        User savedUser = userRepository.findByUsername("newuser").orElse(null);
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches("SecurePass123!", savedUser.getPassword()));
    }

    @Test
    void testRegisterUserDuplicateUsername() {
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("testuser");
        request1.setEmail("user1@example.com");
        request1.setPassword("SecurePass123!");

        authService.registerUser(request1);

        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("testuser");
        request2.setEmail("user2@example.com");
        request2.setPassword("SecurePass123!");

        assertThrows(BadApiRequest.class, () -> authService.registerUser(request2));
    }

    @Test
    void testRegisterUserDuplicateEmail() {
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("user1");
        request1.setEmail("test@example.com");
        request1.setPassword("SecurePass123!");

        authService.registerUser(request1);

        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("user2");
        request2.setEmail("test@example.com");
        request2.setPassword("SecurePass123!");

        assertThrows(BadApiRequest.class, () -> authService.registerUser(request2));
    }

    @Test
    void testRegisterUserEmptyUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("");
        request.setEmail("test@example.com");
        request.setPassword("SecurePass123!");

        assertThrows(BadApiRequest.class, () -> authService.registerUser(request));
    }

    @Test
    void testRegisterUserEmptyEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("");
        request.setPassword("SecurePass123!");

        assertThrows(BadApiRequest.class, () -> authService.registerUser(request));
    }

    @Test
    void testRegisterUserWeakPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("weak");

        assertThrows(BadApiRequest.class, () -> authService.registerUser(request));
    }

    @Test
    void testGetUserByUsernameSuccess() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add(userRole);
        userRepository.save(user);

        User foundUser = authService.getUserByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testGetUserByUsernameNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> authService.getUserByUsername("nonexistent"));
    }

    @Test
    void testIsUsernameExistsTrue() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add(userRole);
        userRepository.save(user);

        assertTrue(authService.isUsernameExists("testuser"));
    }

    @Test
    void testIsUsernameExistsFalse() {
        assertFalse(authService.isUsernameExists("nonexistent"));
    }

    @Test
    void testIsEmailExistsTrue() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add(userRole);
        userRepository.save(user);

        assertTrue(authService.isEmailExists("test@example.com"));
    }

    @Test
    void testIsEmailExistsFalse() {
        assertFalse(authService.isEmailExists("nonexistent@example.com"));
    }
}
