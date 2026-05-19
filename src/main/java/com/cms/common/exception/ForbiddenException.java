package com.cms.common.exception;

public class ForbiddenException
        extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}