package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.TodoRequest;
import com.UCLL.TODO.controller.dto.TodoResponse;
import com.UCLL.TODO.controller.dto.TodoUpdate;
import com.UCLL.TODO.exception.TodoNotFoundException;
import com.UCLL.TODO.model.Todo;
import com.UCLL.TODO.model.TodoStatus;
import com.UCLL.TODO.repository.TodoRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImplementation implements TodoService {
    private TodoRepositoryImplementation todoRepositoryImplementation;

    @Autowired
    public TodoServiceImplementation(TodoRepositoryImplementation todoRepositoryImplementation) {
        this.todoRepositoryImplementation = todoRepositoryImplementation;
    }

    @Override
    public List<TodoResponse> getAllTodosByUserEmail(String userEmail) {
        return this.todoRepositoryImplementation.getAllTodosByUserEmail(userEmail)
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
    public TodoResponse createTodo(TodoRequest todoRequest) {
        Todo newTodo = new Todo(
                todoRequest.title(),
                todoRequest.comment(),
                TodoStatus.NOT_STARTED,
                todoRequest.expiryDate());
        return mapToTodoResponse(this.todoRepositoryImplementation.saveTodo(newTodo));
    }

    @Override
    public TodoResponse updateTodo(long id, TodoUpdate todoUpdate) throws TodoNotFoundException {
        if (!todoRepositoryImplementation.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        Todo todo = new Todo(
                todoUpdate.title(),
                todoUpdate.comment(),
                todoUpdate.status(),
                todoUpdate.expiryDate());
        todo.setTodoId(id);
        return mapToTodoResponse(this.todoRepositoryImplementation.saveTodo(todo));

    }

    @Override
    public void deleteTodo(long todoId) throws TodoNotFoundException {
        if (!todoRepositoryImplementation.existsById(todoId)) {
            throw new TodoNotFoundException(todoId);
        }
        this.todoRepositoryImplementation.deleteTodo(todoId);
    }

    protected TodoResponse mapToTodoResponse(Todo todo) {
        return new TodoResponse(todo.getTodoId(), todo.getTitle(), todo.getComment(), todo.getStatus(), todo.getExpiryDate());
    }
}
