package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:schema.sql")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("Daniel", "Fernandez", "daniel@gmail.com", "password");
    }

    @Test
    public void givenThereExistsAnUser_whenGetUserByEmailIsCalled_thenUserIsReturned() {
        entityManager.persistAndFlush(user);

        var result = userJpaRepository.findUserByEmail(user.getEmail());

        Assertions.assertEquals("Daniel", result.get().getFirstName());
        Assertions.assertEquals("daniel@gmail.com", result.get().getEmail());
    }

    @Test
    public void givenThereIsNoUser_whenGetUserByEmailIsCalled_thenUserIsNotReturned() {
        var result = userJpaRepository.findUserByEmail(user.getEmail());
        Assertions.assertTrue(result.isEmpty());
    }
}
