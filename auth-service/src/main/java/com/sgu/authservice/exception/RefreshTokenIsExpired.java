package com.sgu.authservice.exception;

public class RefreshTokenIsExpired extends RuntimeException {
    public RefreshTokenIsExpired(String message) {
        super(message);
    }
}
