package com.UCLL.TODO.service;

import com.UCLL.TODO.controller.dto.UserRegistration;
import com.UCLL.TODO.controller.dto.UserResponse;
import com.UCLL.TODO.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponse getUserByEmail(String userEmail);
    UserResponse createUser(UserRegistration user);
    UserResponse updateUserById(long id, UserRegistration userUpdate);
    void deleteUserById(long id);
}
