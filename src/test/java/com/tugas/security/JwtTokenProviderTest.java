package com.tugas.security;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private String testUsername;
    private String validToken;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        tokenProvider = new JwtTokenProvider();
        
        // Set secret key via reflection since it's injected
        Field jwtSecretField = JwtTokenProvider.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(tokenProvider, "mySecretKeyForJWTTokenGenerationAndValidationPurposeOnly123456789");
        
        // Set expiration time
        Field jwtExpirationField = JwtTokenProvider.class.getDeclaredField("jwtExpirationMs");
        jwtExpirationField.setAccessible(true);
        jwtExpirationField.set(tokenProvider, 86400000L);
        
        testUsername = "testuser";
        validToken = tokenProvider.generateTokenFromUsername(testUsername);
    }

    @Test
    void testGenerateTokenFromUsername() {
        String token = tokenProvider.generateTokenFromUsername("testuser");
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void testGetUsernameFromJwt() {
        String username = tokenProvider.getUsernameFromJwt(validToken);
        
        assertNotNull(username);
        assertEquals(testUsername, username);
    }

    @Test
    void testValidateToken() {
        boolean isValid = tokenProvider.validateToken(validToken);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithInvalidToken() {
        boolean isValid = tokenProvider.validateToken("invalid.token.here");
        
        assertFalse(isValid);
    }

    @Test
    void testGetUsernameFromInvalidToken() {
        String username = tokenProvider.getUsernameFromJwt("invalid.token.here");
        
        assertNull(username);
    }

    @Test
    void testGenerateTokenForDifferentUsers() {
        String token1 = tokenProvider.generateTokenFromUsername("user1");
        String token2 = tokenProvider.generateTokenFromUsername("user2");
        
        assertNotEquals(token1, token2);
        assertEquals("user1", tokenProvider.getUsernameFromJwt(token1));
        assertEquals("user2", tokenProvider.getUsernameFromJwt(token2));
    }

    @Test
    void testGetTokenFromBearerString() {
        String bearerToken = "Bearer " + validToken;
        String token = tokenProvider.getTokenFromBearerString(bearerToken);
        
        assertNotNull(token);
        assertEquals(validToken, token);
    }

    @Test
    void testGetTokenFromBearerStringInvalid() {
        String wrongFormat = "InvalidFormat " + validToken;
        String token = tokenProvider.getTokenFromBearerString(wrongFormat);
        
        assertNull(token);
    }

    @Test
    void testGetTokenFromBearerStringNullInput() {
        String token = tokenProvider.getTokenFromBearerString(null);
        
        assertNull(token);
    }
}
