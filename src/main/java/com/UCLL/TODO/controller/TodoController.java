package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.TodoRequest;
import com.UCLL.TODO.controller.dto.TodoResponse;
import com.UCLL.TODO.controller.dto.TodoUpdate;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@Validated
public class TodoController
{
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoResponse> getTodosByEmail(@RequestParam String email) throws UserNotFoundException {
        return todoService.getAllTodosByUserEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse addTodo(@RequestParam String email,@Valid @RequestBody TodoRequest todoRequest) throws UserNotFoundException {
        return todoService.createTodo(email, todoRequest);
    }

    @PutMapping("/{id}")
    public TodoResponse updateTodo(@PathVariable long id,@Valid @RequestBody TodoUpdate todoUpdate) throws UserNotFoundException {
        return todoService.updateTodo(id, todoUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable long id) throws UserNotFoundException {
        todoService.deleteTodo(id);
    }
}
