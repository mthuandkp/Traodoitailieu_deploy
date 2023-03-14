package com.sgu.userservice.exception;

public class BadGateWayException extends RuntimeException {
    public BadGateWayException(String message) {
        super(message);
    }
}
