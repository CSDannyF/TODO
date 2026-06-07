package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.TodoRequest;
import com.UCLL.TODO.controller.dto.TodoResponse;
import com.UCLL.TODO.controller.dto.TodoUpdate;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.AuditService;
import com.UCLL.TODO.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/todos")
@Validated
public class TodoController
{
    private TodoService todoService;
    private AuditService auditService;
    private HttpServletRequest httpServletRequest;

    public TodoController(TodoService todoService, AuditService auditService, HttpServletRequest httpServletRequest) {
        this.todoService = todoService;
        this.auditService = auditService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    public List<TodoResponse> getTodosByEmail(Authentication authentication) throws UserNotFoundException {
        auditService.sendAuditMessage(authentication.getName(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        return todoService.getAllTodosByUserEmail(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse addTodo(@Valid @RequestBody TodoRequest todoRequest,Authentication authentication) throws UserNotFoundException {
        auditService.sendAuditMessage(authentication.getName(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        return todoService.createTodo(authentication.getName(), todoRequest);
    }

    @PutMapping("/{id}")
    public TodoResponse updateTodo(@PathVariable long id,@Valid @RequestBody TodoUpdate todoUpdate, Authentication authentication) throws UserNotFoundException {
        auditService.sendAuditMessage(authentication.getName(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        return todoService.updateTodo(id, todoUpdate, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable long id, Authentication authentication) throws UserNotFoundException {
        auditService.sendAuditMessage(authentication.getName(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        todoService.deleteTodo(id, authentication.getName());
    }
}
