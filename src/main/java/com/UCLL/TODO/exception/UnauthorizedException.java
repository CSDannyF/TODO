package com.UCLL.TODO.exception;

import com.UCLL.TODO.model.User;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String email) {
        super("User " + email + " is not authorized");
    }
}
