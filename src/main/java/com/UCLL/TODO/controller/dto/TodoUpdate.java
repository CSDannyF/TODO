package com.UCLL.TODO.controller.dto;

import com.UCLL.TODO.model.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

// Input update
public record TodoUpdate(
        @NotBlank String title,
        String comment,
        @NotNull TodoStatus status,
        @NotNull Date expiryDate
        ) {}
