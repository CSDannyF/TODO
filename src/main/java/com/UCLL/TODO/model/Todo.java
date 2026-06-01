package com.UCLL.TODO.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long todoId;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column
    private String comment;

    @NotNull
    @Column(nullable = false)
    private TodoStatus status;

    @NotNull
    @Column(nullable = false)
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Todo() {}
    public Todo(String title, String comment, TodoStatus status, Date expiryDate) {
        this.title = title;
        this.comment = comment;
        this.status = status;
        this.expiryDate = expiryDate;
    }

    public long getTodoId() {
        return todoId;
    }

    public void setTodoId(long id) { this.todoId = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
