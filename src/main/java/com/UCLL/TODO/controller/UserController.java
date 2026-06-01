package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;


@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final View error;
    private UserService userService;

    public UserController(UserService userService, View error) {
        this.userService = userService;
        this.error = error;
    }

    @GetMapping
    public UserResponse getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRegistration user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable long id,@Valid @RequestBody UserRegistration user) {
        return userService.updateUserById(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) throws UserNotFoundException {
        userService.deleteUserById(id);
    }
}
