package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByEmail(String email);
    User saveUser(User user);
    void deleteUserById(long id);
    boolean existsById(long id);
}
