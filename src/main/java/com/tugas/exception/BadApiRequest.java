package com.tugas.exception;

public class BadApiRequest extends RuntimeException {

    public BadApiRequest(String message) {
        super(message);
    }

    public BadApiRequest(String message, Throwable cause) {
        super(message, cause);
    }
}
