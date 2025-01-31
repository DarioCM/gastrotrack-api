package dev.dario.gastrotrackapi.dailydietlog.controller;

import dev.dario.gastrotrackapi.dailydietlog.dto.DailyDietLogDto;
import dev.dario.gastrotrackapi.dailydietlog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.dailydietlog.service.DailyDietLogService;
import dev.dario.gastrotrackapi.jwt.models.UserPrincipal;
import dev.dario.gastrotrackapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;



@RestController
@RequestMapping("/api/v1/daily-diet-logs")
@RequiredArgsConstructor
@Log4j2
@PreAuthorize("isAuthenticated()")
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
    // userPrincipal is the authenticated user from the JWT token
    @GetMapping
    public Page<DailyDietLogDto> getAllByUserId(
            Pageable pageable,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        int toSkip = pageable.getPageSize() * pageable.getPageNumber();

        UUID userId = userPrincipal.getId();
        List<DailyDietLogEntity> logs = service.findAllByUserId(userId);

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

        log.info("Retrieved daily diet logs for user {}: {}", userId, dtoList);

        return new PageImpl<>(dtoList, pageable, logs.size());
    }

    // POST a new daily diet log
    // POST  /api/v1/users/:userId/daily-diet-logs/
    @PostMapping
    public ResponseEntity<DailyDietLogDto> addDailyDietLog(
            @Valid @RequestBody DailyDietLogDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        UUID userId = userPrincipal.getId();
        // Validate user existence
        userService.findOrThrow(userId);
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
    public ResponseEntity<Void> removeDailyDietLog(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        UUID userId = userPrincipal.getId();
        service.verifyOwnership(id, userId);
        service.removeDailyDietLog(id);
        return ResponseEntity.noContent().build();
    }

    // PUT to update the log
    // PUT /api/v1/daily-diet-logs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDailyDietLog(
            @PathVariable("id") UUID id,
            @Valid @RequestBody DailyDietLogDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        service.verifyOwnership(id, userPrincipal.getId());

        if (!id.equals(dto.getId())) throw
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "id does not match.");

        var entity = convertToEntity(dto);
        service.updateDailyDietLog(id, entity);

        return ResponseEntity.noContent().build();

    }







}
