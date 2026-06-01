package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.Todo;
import com.UCLL.TODO.model.TodoStatus;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.jpa.TodoJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

@DataJpaTest
@Sql("classpath:schema.sql")
public class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoJpaRepository todoJpaRepository;

    private User user;
    private Todo todo;

    @BeforeEach
    void setUp() {
        user = new User("Daniel", "Fernandez", "daniel@gmail.com", "password");
        todo = new Todo("Opruimen", "Tuinhuis opruimen", TodoStatus.NOT_STARTED, new Date());
        todo.setUser(user);
    }

    @Test
    public void givenThereExistsAnUser_whenFindTodosByUserEmailIsCalled_thenTodosAreReturned() {
        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(todo);

        var result = todoJpaRepository.findTodosByUser_Email(user.getEmail());

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(TodoStatus.NOT_STARTED, result.getFirst().getStatus());
        Assertions.assertEquals(todo.getTitle(), result.getFirst().getTitle());
    }

    @Test
    public void givenUserEmailDoesNotExists_whenFindTodosByUserEmailIsCalled_thenNothingIsReturned() {
        var result = todoJpaRepository.findTodosByUser_Email(user.getEmail());

        Assertions.assertTrue(result.isEmpty());
    }
}
