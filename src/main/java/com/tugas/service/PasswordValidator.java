package com.tugas.service;

import org.springframework.stereotype.Service;

/**
 * Service untuk validasi password strength (kompleksitas password)
 */
@Service
public class PasswordValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    /**
     * Validasi kekuatan password
     * Requirements:
     * - Minimal 8 karakter
     * - Minimal 1 huruf besar
     * - Minimal 1 huruf kecil
     * - Minimal 1 angka
     * - Minimal 1 karakter spesial (@$!%*?&)
     *
     * @param password password yang akan divalidasi
     * @return true jika password memenuhi kriteria, false sebaliknya
     */
    public boolean isPasswordValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        return password.length() >= MIN_PASSWORD_LENGTH &&
                password.matches(PASSWORD_PATTERN);
    }

    /**
     * Mendapatkan pesan error detail untuk password yang tidak valid
     *
     * @param password password yang akan divalidasi
     * @return pesan error atau null jika password valid
     */
    public String getPasswordValidationError(String password) {
        if (password == null || password.isEmpty()) {
            return "Password tidak boleh kosong";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password minimal 8 karakter";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password harus mengandung minimal 1 huruf besar (A-Z)";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password harus mengandung minimal 1 huruf kecil (a-z)";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password harus mengandung minimal 1 angka (0-9)";
        }

        if (!password.matches(".*[@$!%*?&].*")) {
            return "Password harus mengandung minimal 1 karakter spesial (@$!%*?&)";
        }

        return null;
    }
}
