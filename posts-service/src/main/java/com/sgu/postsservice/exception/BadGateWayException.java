package com.sgu.postsservice.exception;

public class BadGateWayException extends RuntimeException {
    public BadGateWayException(String message) {
        super(message);
    }
}
