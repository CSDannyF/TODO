package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.TodoRequest;
import com.UCLL.TODO.controller.dto.TodoUpdate;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.TodoNotFoundException;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.model.Todo;
import com.UCLL.TODO.model.TodoStatus;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TodoServiceImplementation todoService;

    private User existingUser;
    private UserResponse userResponse;
    private Todo todo;
    private TodoRequest todoRequest;
    private TodoUpdate todoUpdate;

    @BeforeEach
    void setup() {
        existingUser = new User("Daniel", "Fernandez", "example@gmail.com", "password");
        userResponse = new UserResponse(1, "Daniel", "Fernandez", "example@gmail.com");
        todo = new Todo("Opruimen", "Tuinhuis opruimen",TodoStatus.NOT_STARTED , new Date());
        todoRequest = new TodoRequest("Opruimen", "Tuinhuis opruimen", new Date());
        todoUpdate = new TodoUpdate("Opruimen", "Tuinhuis opruimen", TodoStatus.DONE, new Date());
    }

    @Test
    public void givenUserDoesNotExists_whenGetAllTodosByEmailIsCalled_thenUserNotFoundExceptionIsThrown() {
        Mockito.when(userService.getUserByEmail("example@gmail.com")).thenThrow(new UserNotFoundException("example@gmail.com"));

        var exception = Assertions.assertThrows(UserNotFoundException.class, () -> todoService.getAllTodosByUserEmail("example@gmail.com"));

        Assertions.assertEquals("User not found with email: example@gmail.com", exception.getMessage());
        Mockito.verify(todoRepository, Mockito.never()).getAllTodosByUserEmail("example@gmail.com");
    }

    @Test
    public void givenUserDoesExistsAndTodoWasAdded_whenGetAllTodosByEmailIsCalled_thenTodosAreReturned() {
        Mockito.when(userService.getUserByEmail("example@gmail.com")).thenReturn(userResponse);
        Mockito.when(todoRepository.getAllTodosByUserEmail("example@gmail.com")).thenReturn(List.of(todo));

        var result = todoService.getAllTodosByUserEmail("example@gmail.com");

        Assertions.assertFalse(result.isEmpty());
        Mockito.verify(todoRepository).getAllTodosByUserEmail("example@gmail.com");
    }

    @Test
    public void givenUserDoesExistAndTodoIsCreated_whenCreateTodoIsCalled_thenTodoIsCreated() {
        Mockito.when(userService.getUserByEmail("example@gmail.com")).thenReturn(userResponse);
        Mockito.when(todoRepository.saveTodo(Mockito.any(Todo.class))).thenReturn(todo);
        Mockito.when(userService.mapToUser(Mockito.any(UserResponse.class))).thenReturn(existingUser);

        var result = todoService.createTodo("example@gmail.com", todoRequest);

        Assertions.assertEquals("Opruimen", result.title());
        Assertions.assertEquals(TodoStatus.NOT_STARTED, result.status());

        Mockito.verify(todoRepository).saveTodo(Mockito.any());
    }

    @Test
    public void givenUserDoesNotExistAndTodoIsCreated_whenCreateTodoIsCalled_thenUserNotFoundExceptionIsThrown() {
        Mockito.when(userService.getUserByEmail("example@gmail.com")).thenThrow(new UserNotFoundException("example@gmail.com"));

        var exception = Assertions.assertThrows(UserNotFoundException.class, () -> todoService.createTodo("example@gmail.com", todoRequest));

        Assertions.assertEquals("User not found with email: example@gmail.com", exception.getMessage());
        Mockito.verify(todoRepository, Mockito.never()).saveTodo(Mockito.any());
    }

    @Test void givenUserDoesExistAndTodoIsUpdated_whenUpdateTodoIsCalled_thenTodoIsUpdated() {
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        Mockito.when(todoRepository.saveTodo(Mockito.any(Todo.class))).thenReturn(todo);

        var result = todoService.updateTodo(1, todoUpdate);

        Assertions.assertEquals(TodoStatus.DONE, result.status());
        Mockito.verify(todoRepository).saveTodo(Mockito.any());
    }

    @Test
    public void givenTodoDoesNotExistWhenUpdating_whenUpdateTodoIsCalled_thenTodoNotFoundExceptionIsThrown() {
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(TodoNotFoundException.class, () -> todoService.updateTodo(1, todoUpdate));

        Assertions.assertEquals("Todo not found for id: 1", exception.getMessage());
        Mockito.verify(todoRepository, Mockito.never()).saveTodo(Mockito.any());
    }
}
