package dev.dario.gastrotrackapi.dailyDietLog.repository;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyDietLogRepository extends JpaRepository<DailyDietLogEntity, UUID> {
}
