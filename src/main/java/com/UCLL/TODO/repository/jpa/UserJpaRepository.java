package com.UCLL.TODO.repository.jpa;

import com.UCLL.TODO.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}
