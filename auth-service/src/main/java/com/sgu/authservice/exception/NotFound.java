package com.sgu.authservice.exception;

public class NotFound extends RuntimeException {
    public NotFound(String accountNotExists) {
        super(accountNotExists);
    }
}
