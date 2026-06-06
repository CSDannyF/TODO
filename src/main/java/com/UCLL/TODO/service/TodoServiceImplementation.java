package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.TodoRequest;
import com.UCLL.TODO.controller.dto.TodoResponse;
import com.UCLL.TODO.controller.dto.TodoUpdate;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.TodoNotFoundException;
import com.UCLL.TODO.exception.UnauthorizedException;
import com.UCLL.TODO.model.Todo;
import com.UCLL.TODO.model.TodoStatus;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImplementation implements TodoService {
    private TodoRepository todoRepository;
    private UserService userService;

    @Autowired
    public TodoServiceImplementation(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    @Override
    public List<TodoResponse> getAllTodosByUserEmail(String userEmail) {
        userService.getUserByEmail(userEmail);
        return this.todoRepository.getAllTodosByUserEmail(userEmail)
                .stream()
                .map(todo ->
                        new TodoResponse(
                                todo.getTodoId(),
                                todo.getTitle(),
                                todo.getComment(),
                                todo.getStatus(),
                                todo.getExpiryDate()))
                .toList();
    }

    @Override
    public TodoResponse createTodo(String userEmail, TodoRequest todoRequest) {
        UserResponse userResponse = userService.getUserByEmail(userEmail);
        User user = userService.mapToUser(userResponse);
        Todo newTodo = new Todo(
                todoRequest.title(),
                todoRequest.comment(),
                TodoStatus.NOT_STARTED,
                todoRequest.expiryDate());
        newTodo.setUser(user);
        return mapToTodoResponse(this.todoRepository.saveTodo(newTodo));
    }

    @Override
    public TodoResponse updateTodo(long todoId, TodoUpdate todoUpdate, String userEmail) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));

        if (!todo.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException(userEmail);
        }

        todo.setTitle(todoUpdate.title());
        todo.setComment(todoUpdate.comment());
        todo.setStatus(todoUpdate.status());
        todo.setExpiryDate(todoUpdate.expiryDate());

        return mapToTodoResponse(this.todoRepository.saveTodo(todo));
    }

    @Override
    public void deleteTodo(long todoId, String userEmail) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        if (!todo.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException(userEmail);
        }
        this.todoRepository.deleteTodo(todoId);
    }

    public TodoResponse mapToTodoResponse(Todo todo) {
        return new TodoResponse(todo.getTodoId(), todo.getTitle(), todo.getComment(), todo.getStatus(), todo.getExpiryDate());
    }
}
