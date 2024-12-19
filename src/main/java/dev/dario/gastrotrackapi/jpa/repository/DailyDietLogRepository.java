package dev.dario.gastrotrackapi.jpa.repository;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface DailyDietLogRepository extends JpaRepository<DailyDietLogEntity, UUID> {

    // Custom query method to find all logs by user ID
    List<DailyDietLogEntity> findAllByUserId(UUID userId);


}
