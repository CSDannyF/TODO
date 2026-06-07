package com.UCLL.TODO.model.messaging;

import java.time.LocalDateTime;

public record AuditMessage(String userEmail, String url, String httpMethod, LocalDateTime timeStamp) {
}
