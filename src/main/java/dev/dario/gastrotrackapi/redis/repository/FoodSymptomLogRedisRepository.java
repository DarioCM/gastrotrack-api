package dev.dario.gastrotrackapi.redis.repository;

import dev.dario.gastrotrackapi.foodSymptomLog.entity.FoodSymptomLogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FoodSymptomLogRedisRepository
    extends CrudRepository<FoodSymptomLogEntity, UUID> {
}
