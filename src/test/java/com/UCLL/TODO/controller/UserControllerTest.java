package com.UCLL.TODO.controller;

import com.UCLL.TODO.config.SecurityConfig;
import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.EmailAddressNotUniqueException;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.AuditService;
import com.UCLL.TODO.service.CustomUserDetailsService;
import com.UCLL.TODO.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private AuditService auditService;

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
                .uri("/api/v2/users")
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
                .uri("/api/v2/users")
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
    public void givenDuplicateEmailWithUserRegistration_whenCreateUserIsCalled_thenConfictIsReturned() throws EmailAddressNotUniqueException {
        UserRegistration doubleEmailUser = new UserRegistration("Daniel", "Double", "daniel@gmail.com", "password");
        Mockito.when(userService.createUser(doubleEmailUser)).thenThrow(new EmailAddressNotUniqueException("daniel@gmail.com"));

        client.post()
                .uri("/api/v2/users")
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(doubleEmailUser)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().json(
                        """
                                      {
                                        "message": "A user with e-mail address daniel@gmail.com already exists."
                                      }
                                    """);
        Mockito.verify(userService).createUser(Mockito.any());
    }

    @Test
    @WithMockUser
    public void givenUserWithIdExists_whenDeleteUsersCalled_thenUserIsDeleted() {
        Mockito.when(userService.getUserByEmail("user")).thenReturn(new UserResponse(1, "Daniel", "Fernandez", "user"));

        client.delete()
                .uri("/api/v2/users")
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(userService).deleteUserById(1L);
    }

    @Test
    @WithMockUser
    public void givenUserWithDoesNotExist_whenDeleteUserIsCalled_thenNotFoundIsReturned() {
        Mockito.when(userService.getUserByEmail("user")).thenReturn(new UserResponse(1, "Daniel", "Fernandez", "user"));
        Mockito.doThrow(new UserNotFoundException(1L)).when(userService).deleteUserById(1L);

        client.delete()
                .uri("/api/v2/users")
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
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
