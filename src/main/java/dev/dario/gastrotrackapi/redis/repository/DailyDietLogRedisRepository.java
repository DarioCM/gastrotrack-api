package dev.dario.gastrotrackapi.redis.repository;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface DailyDietLogRedisRepository extends CrudRepository<DailyDietLogEntity, String> {
}
