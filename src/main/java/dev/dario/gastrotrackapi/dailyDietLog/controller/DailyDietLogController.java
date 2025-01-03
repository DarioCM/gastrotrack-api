package dev.dario.gastrotrackapi.dailyDietLog.controller;

import dev.dario.gastrotrackapi.dailyDietLog.dto.DailyDietLogDto;
import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.dailyDietLog.service.DailyDietLogService;
import dev.dario.gastrotrackapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/daily-diet-logs")
@RequiredArgsConstructor
@Log4j2
public class DailyDietLogController {

    private final DailyDietLogService service;
    private final ModelMapper mapper;
    private final UserService userService;

    private DailyDietLogDto convertToDto(DailyDietLogEntity entity) {
        return mapper.map(entity, DailyDietLogDto.class);
    }

    private DailyDietLogEntity convertToEntity(DailyDietLogDto dto) {
        var entity = mapper.map(dto, DailyDietLogEntity.class);
        entity.setDate(LocalDate.parse(dto.getDate()));
        return entity;
    }

    // Get all logs for a user
    // GET /api/v1/daily-diet-logs/123e4567-e89b-12d3-a456-426614174000
    @GetMapping("/{userId}")
    public ResponseEntity<List<DailyDietLogDto>> getAllByUserId(
            @PathVariable UUID userId,
            Pageable pageable) {

        int toSkip = pageable.getPageSize() * pageable.getPageNumber();

        List<DailyDietLogEntity> logs = service.findAllByUserId(userId);
        log.info("Daily diet logs: {}", logs);

        List<DailyDietLogDto> dtoList = logs.stream()
                .map(log -> new DailyDietLogDto(
                        log.getId(),
                        log.getDate().toString(),
                        log.getMeals(),
                        log.getTypeMeal(),
                        log.getNotes(),
                        log.getUser().getId().toString()
                ))
                .skip(toSkip).limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // POST a new daily diet log
    // POST  /api/v1/daily-diet-logs
    @PostMapping("/{id}")
    public ResponseEntity<DailyDietLogDto> addDailyDietLog(
            @PathVariable("id") UUID userId,
            @Valid @RequestBody DailyDietLogDto dto) {

        dto.setUserId(userId);
        log.info("Adding daily diet log DTO enter: {}", dto);
        var entity = convertToEntity(dto);
        log.info("Adding daily diet log entity: {}", entity);
        var log = service.addDailyDietLog(entity);
        var responseDto = convertToDto(log);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // DELETE
    // DELETE /api/v1/daily-diet-logs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDailyDietLog(@PathVariable("id") UUID id) {
        service.removeDailyDietLog(id);
        return ResponseEntity.noContent().build();
    }

    // PUT to update the log
    // PUT /api/v1/daily-diet-logs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDailyDietLog(
            @PathVariable("id") UUID id,
            @Valid @RequestBody DailyDietLogDto dto) {

        if (!id.equals(dto.getId())) throw
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "id does not match.");

        var entity = convertToEntity(dto);
        service.updateDailyDietLog(id, entity);

        return ResponseEntity.noContent().build();

    }







}
