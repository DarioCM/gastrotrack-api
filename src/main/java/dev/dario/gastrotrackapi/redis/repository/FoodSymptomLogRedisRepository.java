package dev.dario.gastrotrackapi.redis.repository;

import dev.dario.gastrotrackapi.foodsymptomlog.entity.FoodSymptomLogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FoodSymptomLogRedisRepository
    extends CrudRepository<FoodSymptomLogEntity, UUID> {
}
