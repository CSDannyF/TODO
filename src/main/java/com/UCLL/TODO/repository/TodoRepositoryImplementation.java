package com.UCLL.TODO.repository;

import com.UCLL.TODO.repository.jpa.TodoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepositoryImplementation implements TodoRepository {
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    public TodoRepositoryImplementation(TodoJpaRepository todoJpaRepository) {
        this.todoJpaRepository = todoJpaRepository;
    }
}
