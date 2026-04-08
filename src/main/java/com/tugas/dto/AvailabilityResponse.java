package com.tugas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk response availability check (username/email)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {
    private String username;
    private String email;
    private boolean available;
}
