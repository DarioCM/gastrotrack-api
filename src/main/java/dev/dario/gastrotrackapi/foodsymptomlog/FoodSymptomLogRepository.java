package dev.dario.gastrotrackapi.foodsymptomlog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface FoodSymptomLogRepository extends JpaRepository<FoodSymptomLogEntity, UUID> {

    List<FoodSymptomLogEntity> getAllByUserId(UUID userId);


}
