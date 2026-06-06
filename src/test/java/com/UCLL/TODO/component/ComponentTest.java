package com.UCLL.TODO.component;

import com.UCLL.TODO.model.Todo;
import com.UCLL.TODO.model.TodoStatus;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.jpa.TodoJpaRepository;
import com.UCLL.TODO.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("dev")
@Sql("classpath:schema.sql")
public class ComponentTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private User user;
    private Todo todo;

    @BeforeEach
    void setUp() {
        user = new User("Daniel", "Fernandez", "daniel@gmail.com", encoder.encode("password"));
        todo = new Todo("Opruimen", "Tuinhuis opruimen", TodoStatus.NOT_STARTED, new Date());
        todo.setUser(user);

        userJpaRepository.save(user);
        todoJpaRepository.save(todo);
    }

    @AfterEach
    void tearDown() {
        todoJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    public void givenUserIsInDb_whenCallingGetUserByEmail_thenUserIsReturned() {
        client.get()
                .uri("/api/v2/users/me", user.getEmail())
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""

                        {
                                "userId": 1,
                                "firstName": "Daniel",
                                "lastName": "Fernandez",
                                "email": "daniel@gmail.com"
                        }
                        """
                );
    }

    @Test
    public void givenUserIsPostedInDb_whenCallingCreateUser_thenUserIsPosted() {
        client.post()
                .uri("/api/v2/users")
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .bodyValue(
                        """
                              {
                                "firstName": "Boke",
                                "lastName": "Fernandez",
                                "email": "Boke@gmail.com",
                                "password": "password"
                              }
                        """
                )
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json(
                        """
                                {
                                  "firstName": "Boke",
                                  "lastName": "Fernandez",
                                  "email": "Boke@gmail.com"
                                }
                          """
                );

        var result = userJpaRepository.findUserByEmail("Boke@gmail.com");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Boke@gmail.com", result.get().getEmail());
    }

    @Test
    public void givenUserInDatabaseIsUpdated_whenCallingUpdateUser_thenUserIsUpdated() {
        client.put()
                .uri("/api/v2/users")
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .bodyValue(
                        """
                                {
                                    "firstName": "Boke",
                                    "lastName": "Fernandez",
                                    "email": "boke@gmail.com",
                                    "password": "password"
                                }
                        """
                )
                .exchange()
                .expectBody()
                .json(
                                """
                                {
                                  "firstName": "Boke",
                                  "lastName": "Fernandez",
                                  "email": "boke@gmail.com"
                                }
                          """);

        var result = userJpaRepository.findUserByEmail("boke@gmail.com");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Boke", result.get().getFirstName());
        Assertions.assertEquals("boke@gmail.com", result.get().getEmail());
    }

    @Test
    public void givenUserExists_whenDeleteByIdIsCalled_thenUserIsDeleted() {
        client.delete()
                .uri("/api/v2/users")
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .exchange()
                .expectStatus().is2xxSuccessful();

        var result = userJpaRepository.findUserByEmail("daniel@gmail.com");
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void givenTodosInDb_whenGetTodosByEmailIsCalled_thenTodosAreReturned() {
        client.get()
                .uri("/api/v2/todos?email={email}", user.getEmail())
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json(
                        """
                                [
                                {
                                     "todoId": 1,
                                     "title": "Opruimen",
                                     "comment": "Tuinhuis opruimen",
                                     "status": "NOT_STARTED"
                                }
                                ]
                                """
                );
    }

    @Test
    public void givenNewTodo_whenCreateTodoIsCalled_thenTodoIsPosted() {
        client.post()
                .uri("/api/v2/todos")
                .headers(headers -> {
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .bodyValue("""
                          {
                             "title": "Ramen wassen",
                             "comment": "Ramen wassen buitenkant",
                             "status": "NOT_STARTED",
                             "expiryDate": "2026-08-19"
                          }
                          """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json(
                        """ 
                          {
                             "title": "Ramen wassen",
                             "comment": "Ramen wassen buitenkant",
                             "status": "NOT_STARTED"
                          }
                          """
                );

        var result = todoJpaRepository.findTodosByUser_Email(user.getEmail());
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Ramen wassen", result.get(1).getTitle());
    }

    @Test
    public void givenTodoToUpdate_whenUpdateTodoIsCalled_TodoIsUpdated() {
        client.put()
                .uri("/api/v2/todos/{id}", todo.getTodoId())
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .bodyValue("""
                          {
                             "title": "Opruimen",
                             "comment": "Tuinhuis opruimen",
                             "status": "DONE",
                             "expiryDate": "2026-08-19"
                          }
                          """
                )
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json(
                        """
                                 {
                                     "todoId": 1,
                                      "title": "Opruimen",
                                      "comment": "Tuinhuis opruimen",
                                      "status": "DONE"
                                 }
                                 """
                );

        var result = todoJpaRepository.findTodosByUser_Email(user.getEmail());
        Assertions.assertEquals(TodoStatus.DONE, result.getFirst().getStatus());
    }

    @Test
    public void givenTodoIdToDelete_whenCallingDeleteTodoById_thenTodoIsDeleted() {
        client.delete()
                .uri("/api/v2/todos/{id}", todo.getTodoId())
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth("daniel@gmail.com", "password");
                })
                .exchange()
                .expectStatus().is2xxSuccessful();

        var result = todoJpaRepository.findTodosByUser_Email(user.getEmail());

        Assertions.assertTrue(result.isEmpty());
    }
}
