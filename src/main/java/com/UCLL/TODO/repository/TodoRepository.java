package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    List<Todo> getAllTodosByUserEmail(String email);
    Optional<Todo> findById(long id);
    Todo saveTodo(Todo todo);
    void deleteTodo(long id);
    boolean existsById(long id);
}
