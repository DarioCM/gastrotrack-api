package dev.dario.gastrotrackapi.foodSymptomLog.repository;

import dev.dario.gastrotrackapi.foodSymptomLog.entity.FoodSymptomLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodSymptomLogRepository extends JpaRepository<FoodSymptomLogEntity, UUID> {
}
