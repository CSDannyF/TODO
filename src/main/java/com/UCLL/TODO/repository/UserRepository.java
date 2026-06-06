package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAllUsers();
    Optional<User> getUserByEmail(String email);
    Optional<User> findById(long id);
    User saveUser(User user);
    void deleteUserById(long id);
    boolean existsById(long id);
}
