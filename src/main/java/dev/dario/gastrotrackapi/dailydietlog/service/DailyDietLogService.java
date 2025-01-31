package dev.dario.gastrotrackapi.dailydietlog.service;

import dev.dario.gastrotrackapi.dailydietlog.dto.DailyDietLogDto;
import dev.dario.gastrotrackapi.dailydietlog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.exception.NotFoundException;
import dev.dario.gastrotrackapi.jpa.repository.DailyDietLogRepository;
import dev.dario.gastrotrackapi.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class DailyDietLogService {

    private final DailyDietLogRepository repository;
    private final UserService userService;
    private final ModelMapper mapper;

    //  Convert entity to DTO
    public DailyDietLogDto convertToDto(DailyDietLogEntity entity) {
        return mapper.map(entity, DailyDietLogDto.class);
    }

    //  Convert DTO to entity
    public DailyDietLogEntity convertToEntity(DailyDietLogDto dto) {
        var entity = mapper.map(dto, DailyDietLogEntity.class);
        entity.setDate(LocalDate.parse(dto.getDate()));
        return entity;
    }


    // findAll
    // Get all logs for a user by user ID
    @Transactional(readOnly = true)
    public Page<DailyDietLogDto> findAllByUserId(UUID userId, Pageable pageable) {
        log.info("Finding logs for user: {}", userId);
        Page<DailyDietLogEntity> logs = repository.findAllByUserId(userId, pageable);
        return logs.map(this::convertToDto);
    }

    // add
    @Transactional
    public DailyDietLogDto addDailyDietLog(DailyDietLogDto dto, UUID userId) {
        log.info("Adding daily diet log: {} , {} ", dto, userId);
        userService.findOrThrow(userId);
        dto.setUserId(userId);
        var entity = convertToEntity(dto);
        var savedLog = repository.save(entity);
        return convertToDto(savedLog);
    }

    // delete
    @Transactional
    public void removeDailyDietLog(UUID id, UUID userId) {
        log.info("Removing daily diet log with ID: {}  , user {}", id, userId);
        findOrThrow(id);
        verifyOwnership(id, userId);
        repository.deleteById(id);
    }

    // update
    @Transactional
    public void updateDailyDietLog(UUID id, DailyDietLogDto dto, UUID userId) {
        log.info("Updating daily diet log with ID: {} , user {} ", id,  userId);
        findOrThrow(id);
        verifyOwnership(id, userId);
        if (!id.equals(dto.getId())) {
            throw new IllegalArgumentException("ID in request body does not match path parameter.");
        }
        var entity = convertToEntity(dto);
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
