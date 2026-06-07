package com.UCLL.TODO.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

//Input - aanmaken Todo
public record TodoRequest(
        @NotBlank String title,
        String comment,         // optioneel
        @NotNull Date expiryDate
        ) {}
