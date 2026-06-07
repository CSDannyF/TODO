package com.UCLL.TODO.repository;

import com.UCLL.TODO.model.Audit;

public interface AuditRepository {
    Audit saveAudit(Audit audit);
}
