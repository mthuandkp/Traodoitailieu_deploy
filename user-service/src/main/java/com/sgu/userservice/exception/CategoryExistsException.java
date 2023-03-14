package com.sgu.userservice.exception;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException(String s) {
        super(s);
    }
}
