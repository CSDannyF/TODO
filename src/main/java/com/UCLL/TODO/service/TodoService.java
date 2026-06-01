package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.TodoRequest;
import com.UCLL.TODO.controller.dto.TodoResponse;
import com.UCLL.TODO.controller.dto.TodoUpdate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {
    List<TodoResponse> getAllTodosByUserEmail(String userEmail);
    TodoResponse createTodo(String userEmail, TodoRequest todoRequest);
    TodoResponse updateTodo(long id, TodoUpdate todoUpdate);
    void deleteTodo(long todoId);
}
