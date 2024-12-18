package dev.dario.gastrotrackapi.jpa.repository;

import dev.dario.gastrotrackapi.foodSymptomLog.entity.FoodSymptomLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodSymptomLogRepository extends JpaRepository<FoodSymptomLogEntity, UUID> {

    List<FoodSymptomLogEntity> getAllByUserId(UUID userId);


}
