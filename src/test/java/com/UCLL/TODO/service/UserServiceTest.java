package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    public void givenUserExistsWithEmail_whenUserIsSearched_thenUserIsFound() {
        User user = new User("Daniel", "Fernandez", "daniel@gmail.com", "password");

        Mockito.when(userRepository.getUserByEmail("daniel@gmail.com")).thenReturn(Optional.of(user));

        UserResponse result = userService.getUserByEmail("daniel@gmail.com");
        Assertions.assertNotNull(result);
    }

    @Test
    public void givenUserWithEmailDoesNotExists_whenUserIsSearched_thenUserNotFoundExceptionIsThrown() {
        var exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("daniel@nomail.com"));
        Assertions.assertEquals("User not found with email: daniel@nomail.com", exception.getMessage());
    }

    @Test
    public void givenUserExistsAndIsUpdated_whenUpdateUserIsCalled_thenUserIsUpdated() {
        User existingUser = new User("Daniel", "Fernandez", "daniel@gmail.com", "password");
        UserRegistration updateUser = new UserRegistration("Boke", "Fernandez", "boke@gmail.com", "password");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.saveUser(Mockito.any(User.class))).thenReturn(existingUser);

        var result = userService.updateUserById(1L, updateUser);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateUser.firstName(), result.firstName());
        Assertions.assertEquals(updateUser.email(), result.email());

        Mockito.verify(userRepository).saveUser(Mockito.any(User.class));
    }

    @Test
    public void givenUserDoesNotExistAndIsUpdated_whenUpdateUserIsCalled_thenUserNotFoundExceptionIsThrown() {
        UserRegistration updateUser = new UserRegistration("", "Fernandez", "boke@gmail.com", "password");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUserById(1L, updateUser));

        Assertions.assertEquals("User not found with id: 1", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).saveUser(Mockito.any());
    }

    @Test
    public void givenUserExists_whenDeleteByIdIsCalled_thenUserIsDeleted() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUserById(1L);
        Mockito.verify(userRepository).deleteUserById(1L);
    }

    @Test
    public void givenUserDoesNotExistsAndIsDeleted_whenDeleteByIdIsCalled_thenUserNotfoundExceptionIsThrown() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);
        var exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));

        Assertions.assertEquals("User not found with id: 1", exception.getMessage());
    }
}
