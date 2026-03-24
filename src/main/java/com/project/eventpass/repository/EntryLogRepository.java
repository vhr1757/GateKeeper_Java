package com.project.eventpass.repository;

import com.project.eventpass.entity.EntryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {}
