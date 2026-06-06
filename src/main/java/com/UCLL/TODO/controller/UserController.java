package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.EmailAddressNotUniqueException;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/v2/users")
@Validated
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(@PathVariable long id) {
        System.out.println(id);
        System.out.println(userService.getUserById(id));
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public UserResponse getLoggedInUser(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRegistration user) throws EmailAddressNotUniqueException {
        return userService.createUser(user);
    }

    @PutMapping
    public UserResponse updateUser(@Valid @RequestBody UserRegistration user,
                                   Authentication authentication) {
        var authUser = userService.getUserByEmail(authentication.getName());
        return userService.updateUserById(authUser.userId(), user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication) throws UserNotFoundException {
        var authUser = userService.getUserByEmail(authentication.getName());
        userService.deleteUserById(authUser.userId());
    }
}
