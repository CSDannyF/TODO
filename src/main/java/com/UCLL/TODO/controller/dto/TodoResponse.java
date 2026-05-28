package com.UCLL.TODO.controller.dto;

import com.UCLL.TODO.model.TodoStatus;

import java.util.Date;

// Output Get
public record TodoResponse(
        long todoId,
        String title,
        String comment,
        TodoStatus status,
        Date expiryDate
) {}
