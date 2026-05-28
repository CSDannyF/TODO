package com.UCLL.TODO.controller.dto;

// Output get
public record UserResponse(
        long userId,
        String firstName,
        String lastName,
        String email
) {}
