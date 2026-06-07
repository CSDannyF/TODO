package com.UCLL.TODO.model.messaging;

import java.io.Serializable;
import java.time.LocalDateTime;

public record AuditMessage(String userEmail, String url, String httpMethod, LocalDateTime timeStamp) {
}
