package dev.dario.gastrotrackapi.redis.repository;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DailyDietLogRedisRepository extends CrudRepository<DailyDietLogEntity, UUID> {
}
