package com.UCLL.TODO.repository.jpa;

import com.UCLL.TODO.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoJpaRepository extends JpaRepository<Todo, Long> {
    List<Todo> findTodosByUser_Email(String userEmail);

}
