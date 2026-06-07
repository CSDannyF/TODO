package com.UCLL.TODO.repository.jpa;

import com.UCLL.TODO.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditJpaRepository extends JpaRepository<Audit, Long> {
}
