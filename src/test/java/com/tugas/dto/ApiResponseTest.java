package com.tugas.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    private ApiResponse response;

    @BeforeEach
    void setUp() {
        response = new ApiResponse();
    }

    @Test
    void testApiResponseCreation() {
        response.setSuccess(true);
        response.setMessage("Operation successful");
        response.setData("test data");
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertEquals("test data", response.getData());
    }

    @Test
    void testApiResponseWithErrors() {
        response.setSuccess(false);
        response.setMessage("Error occurred");
        response.setData(null);
        
        assertFalse(response.getSuccess());
        assertEquals("Error occurred", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testApiResponseConstructorWithMessage() {
        ApiResponse resp = new ApiResponse(true, "Success");
        
        assertTrue(resp.getSuccess());
        assertEquals("Success", resp.getMessage());
    }

    @Test
    void testApiResponseFullConstructor() {
        ApiResponse resp = new ApiResponse(true, "Created", 12345L);
        
        assertTrue(resp.getSuccess());
        assertEquals("Created", resp.getMessage());
        assertEquals(12345L, resp.getData());
    }
}
