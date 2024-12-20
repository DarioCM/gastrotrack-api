package dev.dario.gastrotrackapi.dailyDietLog.service;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.jpa.repository.DailyDietLogRepository;
import dev.dario.gastrotrackapi.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class DailyDietLogService {

    private final DailyDietLogRepository repository;

    // findAll
    public Iterable<DailyDietLogEntity> findAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    // add
    public DailyDietLogEntity addDailyDietLog(DailyDietLogEntity entity) {
        return repository.save(entity);
    }

    // delete
    public void removeDailyDietLog(UUID id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    // update
    public void updateDailyDietLog(UUID id, DailyDietLogEntity entity) {
        findOrThrow(id);
        repository.save(entity);
    }

    // findOrThrow
    private DailyDietLogEntity findOrThrow(final UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("DailyDietLog not found"));
    }
}
