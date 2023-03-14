package com.sgu.postsservice.exception;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException(String s) {
        super(s);
    }
}
