package com.UCLL.TODO.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Input registratie
public record UserRegistration(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank String password
) {}
