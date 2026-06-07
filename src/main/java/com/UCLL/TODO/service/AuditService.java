package com.UCLL.TODO.service;

public interface AuditService {
    void sendAuditMessage(String userEmail, String url, String httpMethod);
}
