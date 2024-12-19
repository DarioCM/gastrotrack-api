package dev.dario.gastrotrackapi.dailyDietLog.controller;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.dailyDietLog.record.DailyDietLogRecord;
import dev.dario.gastrotrackapi.dailyDietLog.service.DailyDietLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/daily-diet-logs")
@RequiredArgsConstructor
public class DailyDietLogController {

    private final DailyDietLogService service;

    private final ModelMapper mapper;

    private DailyDietLogRecord convertToDto(DailyDietLogEntity entity) {
        return mapper.map(entity, DailyDietLogRecord.class);
    }

    private DailyDietLogEntity convertToEntity(DailyDietLogRecord dto) {
        return mapper.map(dto, DailyDietLogEntity.class);
    }

    // Get all logs for a user
    @GetMapping("/{userId}")
    public List<DailyDietLogRecord> getAllByUserId(@PathVariable UUID userId) {
        var logList = StreamSupport
                .stream(
                        service.findAllByUserId(userId).spliterator(), false)
                .collect(Collectors.toList());
        return logList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // POST a new daily diet log
    @PostMapping
    public DailyDietLogRecord addDailyDietLog(
            @Valid @RequestBody DailyDietLogRecord dto) {
        var entity = convertToEntity(dto);
        var log = service.addDailyDietLog(entity);
        return convertToDto(log);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void removeDailyDietLog(@PathVariable("id") UUID id) {
        service.removeDailyDietLog(id);
    }

    // PUT to update the log
    @PutMapping("/{id}")
    public void updateDailyDietLog(
            @PathVariable("id") UUID id,
            @Valid @RequestBody DailyDietLogRecord dto) {

        if (!id.equals(dto.id())) throw
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "id does not match.");

        var entity = convertToEntity(dto);
        service.updateDailyDietLog(id, entity);

    }







}
