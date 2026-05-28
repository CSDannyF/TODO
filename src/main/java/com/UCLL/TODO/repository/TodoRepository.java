package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.Todo;

import java.util.List;

public interface TodoRepository {
    List<Todo> getAllTodosByUserEmail(String email);
    Todo saveTodo(Todo todo);
    void deleteTodo(long id);
    boolean existsById(long id);
}
