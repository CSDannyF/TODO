package com.UCLL.TODO.exception;

public class EmailAddressNotUniqueException extends Exception {
    public EmailAddressNotUniqueException(String emailAddress) {
        super("A user with e-mail address " + emailAddress + " already exists.");
    }
}
