package com.UCLL.TODO.service;

import com.UCLL.TODO.config.RabbitConfig;
import com.UCLL.TODO.model.messaging.AuditMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImplementation implements AuditService {

    private final RabbitTemplate rabbitTemplate;

    public AuditServiceImplementation(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Stuurt een auditMessage naar de queue wanneer deze wordt aangeroepen
    public void sendAuditMessage(String userEmail, String url, String httpMethod) {
        final var message = new AuditMessage(userEmail, url, httpMethod, LocalDateTime.now());
        rabbitTemplate.convertAndSend(
                RabbitConfig.AUDIT_QUEUE,
                message);
    }
}
