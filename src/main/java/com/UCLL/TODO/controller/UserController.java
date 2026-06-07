package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.exception.EmailAddressNotUniqueException;
import com.UCLL.TODO.exception.UserNotFoundException;
import com.UCLL.TODO.service.AuditService;
import com.UCLL.TODO.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private AuditService auditService;
    private HttpServletRequest httpServletRequest;

    public UserController(UserService userService, AuditService auditService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.auditService = auditService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(Authentication authentication) {
        auditService.sendAuditMessage(authentication.getName(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(@PathVariable long id, Authentication authentication) {
        auditService.sendAuditMessage(authentication.getName(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public UserResponse getLoggedInUser(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRegistration user) throws EmailAddressNotUniqueException {
        auditService.sendAuditMessage(user.email(), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
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
