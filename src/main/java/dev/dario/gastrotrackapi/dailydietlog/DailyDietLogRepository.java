package dev.dario.gastrotrackapi.dailydietlog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyDietLogRepository extends JpaRepository<DailyDietLogEntity, UUID> {

    @Query("SELECT ddl FROM DailyDietLogEntity ddl WHERE ddl.user.id = :userId")
    Page<DailyDietLogEntity> findAllByUserId(@Param("userId") UUID userId, Pageable  pageable);

}
