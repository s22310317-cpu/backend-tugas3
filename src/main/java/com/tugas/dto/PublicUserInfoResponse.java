package com.tugas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk public user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserInfoResponse {
    private String message;
    private long totalUsers;
    private String apiVersion = "1.0.0";

    public PublicUserInfoResponse(String message, long totalUsers) {
        this.message = message;
        this.totalUsers = totalUsers;
    }
}
