package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.Todo;
import com.UCLL.TODO.repository.jpa.TodoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoRepositoryImplementation implements TodoRepository {
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    public TodoRepositoryImplementation(TodoJpaRepository todoJpaRepository) {
        this.todoJpaRepository = todoJpaRepository;
    }

    @Override
    public List<Todo> getAllTodosByUserEmail(String email) {
        return this.todoJpaRepository.findTodosByUser_Email(email);
    }

    @Override
    public Todo saveTodo(Todo todo) {
        return this.todoJpaRepository.save(todo);
    }

    /*@Override
    public Todo updateTodo(long id, Todo todo) {
        return null;
    }*/

    @Override
    public void deleteTodo(long id) {
        this.todoJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return todoJpaRepository.existsById(id);
    }
}
