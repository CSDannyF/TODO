package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.User;
import com.UCLL.TODO.repository.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImplementation implements UserRepository {

    private UserJpaRepository userJpaRepository;

    @Autowired
    public UserRepositoryImplementation(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userJpaRepository.findUserByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return this.userJpaRepository.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return userJpaRepository.existsById(id);
    }
}
