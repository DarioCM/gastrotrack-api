package dev.dario.gastrotrackapi.jpa.repository;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DailyDietLogRepository extends JpaRepository<DailyDietLogEntity, UUID> {

    @Query("SELECT ddl FROM DailyDietLogEntity ddl WHERE ddl.user.id = :userId")
    List<DailyDietLogEntity> findAllByUserId(@Param("userId") UUID userId);
}
