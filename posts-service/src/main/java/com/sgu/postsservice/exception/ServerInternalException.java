package com.sgu.postsservice.exception;

public class ServerInternalException extends RuntimeException {
    public ServerInternalException(String message) {
        super(message);
    }
}
