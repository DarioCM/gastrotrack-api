package dev.dario.gastrotrackapi.foodsymptomlog.service;

import dev.dario.gastrotrackapi.exception.NotFoundException;
import dev.dario.gastrotrackapi.foodsymptomlog.entity.FoodSymptomLogEntity;
import dev.dario.gastrotrackapi.jpa.repository.FoodSymptomLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FoodSymtomLogService {

    private final FoodSymptomLogRepository repository;


    // getAllBYUserID
    public Iterable<FoodSymptomLogEntity> findAllByUserId(UUID userId) {
        return repository.getAllByUserId(userId);
    }

    // add
    public FoodSymptomLogEntity addFoodSymptomLog(FoodSymptomLogEntity entity) {
        return repository.save(entity);
    }

    // remove
    public void removeFoodSymptomLog(UUID id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    // update
    public void updateFoodSymptomLog(UUID id, FoodSymptomLogEntity entity) {
        findOrThrow(id);
        repository.save(entity);
    }


    // findOrThrow
    private FoodSymptomLogEntity findOrThrow(final UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("FoodSymptomLog not found"));
    }


}
