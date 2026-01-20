package com.example.SP26SE025.exception;

/**
 * Exception thrown when uploaded image is invalid (format, size, corrupted)
 */
public class InvalidImageException extends RuntimeException {

    private String reason; // e.g., "INVALID_FORMAT", "FILE_TOO_LARGE", "CORRUPTED"

    public InvalidImageException(String message) {
        super(message);
    }

    public InvalidImageException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageException(String message, String reason, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
