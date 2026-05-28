package com.UCLL.TODO.controller.dto;

import com.UCLL.TODO.model.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

//Input - aanmaken Todo
public record TodoRequest(
        @NotBlank String title,
        String comment,         // optioneel
        @NotNull TodoStatus status,
        @NotNull Date expiryDate,
        @NotBlank String userEmail // verdwijnt in deel 2 bij Spring Security
        ) {}
