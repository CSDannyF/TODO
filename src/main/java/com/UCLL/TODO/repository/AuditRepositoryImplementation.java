package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.Audit;
import com.UCLL.TODO.repository.jpa.AuditJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuditRepositoryImplementation implements AuditRepository {
    private AuditJpaRepository auditJpaRepository;

    @Autowired
    public AuditRepositoryImplementation(AuditJpaRepository auditJpaRepository) {
        this.auditJpaRepository = auditJpaRepository;
    }

    @Override
    public Audit saveAudit(Audit audit) {
        return auditJpaRepository.save(audit);
    }
}
