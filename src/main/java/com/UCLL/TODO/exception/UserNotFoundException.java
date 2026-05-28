package com.UCLL.TODO.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
    public UserNotFoundException(long id) { super("User not found with id: " + id); }
}
