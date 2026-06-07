package com.UCLL.TODO.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column
    private String user;

    @Column
    private String url;

    @Column(name = "http_method")
    private String httpMethod;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long id) {
        this.auditId = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
