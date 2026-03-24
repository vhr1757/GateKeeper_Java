package com.project.eventpass.repository;

import com.project.eventpass.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PassRepository extends JpaRepository<Pass, Long> {
    Optional<Pass> findByPassCode(String passCode);
    List<Pass> findByUserId(Long userId);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    long countByEventId(Long eventId);
}
