package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistration userRegistration;
    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        userRegistration = new UserRegistration("Daniel", "Fernandez", "daniel@gmail.com", "password");
        userResponse = new UserResponse(1, "Daniel", "Fernandez", "daniel@gmail.com");
    }

    @Test
    public void givenValidUserRegistration_whenCreateUserIsCalled_thenUserIsCreatedAndReturned() throws Exception {
        Mockito.when(userService.createUser(userRegistration)).thenReturn(userResponse);

        client.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRegistration)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json(objectMapper.writeValueAsString(userResponse));

        Mockito.verify(userService).createUser(userRegistration);
    }

    @Test
    public void givenInvalidUserRegistration_whenCreateUserIsCalled_thenBadRequestIsReturned() throws Exception {
        userRegistration = new UserRegistration("Daniel", "Fernandez", "invalid", "password");
        client.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRegistration)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json(
        """
                      {
                        "email": "must be a well-formed email address"
                      }
                    """);

        // https://www.javadoc.io/doc/org.mockito/mockito-core/2.7.21/org/mockito/Mockito.html#never_verification
        Mockito.verify(userService, Mockito.never()).createUser(Mockito.any());
    }

    @Test
    public void givenDuplicateEmailWithUserRegistration_whenCreateUserIsCalled_thenConfictIsReturned() {
        UserRegistration doubleEmailUser = new UserRegistration("Daniel", "Double", "daniel@gmail.com", "password");
        Mockito.when(userService.createUser(doubleEmailUser)).thenThrow(DataIntegrityViolationException.class);

        client.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(doubleEmailUser)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().json(
                        """
                                      {
                                        "message": "Email already in use"
                                      }
                                    """);
        Mockito.verify(userService).createUser(Mockito.any());
    }

    @Test
    public void givenUserWithIdExists_whenDeleteUsersCalled_thenUserIsDeleted() {
        client.delete()
                .uri("/api/v1/users/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(userService).deleteUserById(1L);
    }

    @Test
    public void givenUserWithDoesNotExist_whenDeleteUserIsCalled_thenNotFoundIsReturned() {
        Mockito.doThrow(new UserNotFoundException(1L)).when(userService).deleteUserById(1L);

        client.delete()
                .uri("/api/v1/users/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json(
            """
                          {
                            "message": "User not found with id: 1"
                          }
                        """);
    }
}
