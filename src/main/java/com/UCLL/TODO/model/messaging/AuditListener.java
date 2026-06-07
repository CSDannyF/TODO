package com.UCLL.TODO.model.messaging;

import com.UCLL.TODO.config.RabbitConfig;
import com.UCLL.TODO.model.Audit;
import com.UCLL.TODO.repository.AuditRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {
    private AuditRepository auditRepository;

    public AuditListener(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    // Ontvangt de auditMessages van de queue
    @RabbitListener(queues = RabbitConfig.AUDIT_QUEUE)
    public void receiveAuditMessage(AuditMessage auditMessage) {
        Audit audit = new Audit();
        audit.setUser(auditMessage.userEmail());
        audit.setUrl(auditMessage.url());
        audit.setHttpMethod(auditMessage.httpMethod());
        audit.setTimeStamp(auditMessage.timeStamp());

        auditRepository.saveAudit(audit);
    }
}
