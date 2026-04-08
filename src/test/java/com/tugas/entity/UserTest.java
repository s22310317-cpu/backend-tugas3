package com.tugas.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role("ROLE_USER");
        
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedpassword123");
        
        Set<Role> roles = new HashSet<>();
        roles.add(testRole);
        user.setRoles(roles);
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("encodedpassword123", user.getPassword());
        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void testUserSettersAndGetters() {
        user.setUsername("newusername");
        user.setEmail("newemail@example.com");
        user.setPassword("newpassword");
        
        assertEquals("newusername", user.getUsername());
        assertEquals("newemail@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testUserWithMultipleRoles() {
        Role adminRole = new Role("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(testRole);
        roles.add(adminRole);
        user.setRoles(roles);
        
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_USER")));
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserWithEmptyRoles() {
        user.setRoles(new HashSet<>());
        
        assertTrue(user.getRoles().isEmpty());
    }
}
