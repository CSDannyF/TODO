package com.UCLL.TODO.exception;


public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String email) {
        super("User " + email + " is not authorized");
    }
}
