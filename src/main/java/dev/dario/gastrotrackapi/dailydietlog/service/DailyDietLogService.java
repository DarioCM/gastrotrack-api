package dev.dario.gastrotrackapi.dailydietlog.service;

import dev.dario.gastrotrackapi.dailydietlog.dto.DailyDietLogDto;
import dev.dario.gastrotrackapi.dailydietlog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.jpa.repository.DailyDietLogRepository;
import dev.dario.gastrotrackapi.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class DailyDietLogService {

    private final DailyDietLogRepository repository;

    // findAll
    // Get all logs for a user by user ID
    @Transactional(readOnly = true)
    public List<DailyDietLogEntity> findAllByUserId(UUID userId) {
        log.info("Finding logs for user: {}", userId);
        return repository.findAllByUserId(userId);
    }

    // add
    @Transactional
    public DailyDietLogEntity addDailyDietLog(DailyDietLogEntity entity) {
        log.info("Adding daily diet log: {}", entity);
        return repository.save(entity);
    }

    // delete
    @Transactional
    public void removeDailyDietLog(UUID id) {
        log.info("Removing daily diet log with ID: {}", id);
        findOrThrow(id);
        repository.deleteById(id);
    }

    // update
    @Transactional
    public void updateDailyDietLog(UUID id, DailyDietLogEntity entity) {
        log.info("Updating daily diet log with ID: {}", id);
        findOrThrow(id);
        repository.save(entity);
    }

    // findOrThrow
    private DailyDietLogEntity findOrThrow(final UUID id) {
        log.debug("Fetching daily diet log with ID: {}", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException("DailyDietLog not found"));
    }

    public void verifyOwnership(UUID id, UUID userId) {

        DailyDietLogEntity log = repository.findById(id).orElseThrow(
                () -> new NotFoundException("DailyDietLog not found")
        );

        if(!log.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this DailyDietLog");
        }

    }
}

/*
*   1.  Use @Transactional(readOnly = true) for read operations to optimize performance and avoid unnecessary locking.
	2.  Use @Transactional for write operations to enable rollback and maintain data integrity.
*
* */
