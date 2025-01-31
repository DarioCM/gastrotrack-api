package dev.dario.gastrotrackapi.dailydietlog;

import dev.dario.gastrotrackapi.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;



@RestController
@RequestMapping("/api/v1/daily-diet-logs")
@RequiredArgsConstructor
@Log4j2
@PreAuthorize("isAuthenticated()")
public class DailyDietLogController {

    private final DailyDietLogService service;

    // Get all logs for a user
    // userPrincipal is the authenticated user from the JWT token
    @GetMapping
    public Page<DailyDietLogDto> getAllByUserId(
                                                                                            @PageableDefault(size = 10, page = 0, sort = "date", direction = Sort.Direction.DESC) Pageable pageable,
                                                                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return service.findAllByUserId(userPrincipal.getId(), pageable);
    }

    // POST a new daily diet log
    // POST  /api/v1/users/:userId/daily-diet-logs/
    @PostMapping
    public ResponseEntity<DailyDietLogDto> addDailyDietLog(
                                                                                                                @Valid @RequestBody DailyDietLogDto dto,
                                                                                                                @AuthenticationPrincipal UserPrincipal userPrincipal)  {
        DailyDietLogDto savedLog = service.addDailyDietLog(dto, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLog);
    }

    // DELETE
    // DELETE /api/v1/daily-diet-logs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDailyDietLog(
                                                                                                    @PathVariable("id") UUID id,
                                                                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        service.removeDailyDietLog(id, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

    // PUT to update the log
    // PUT /api/v1/daily-diet-logs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDailyDietLog(
                                                                                                    @PathVariable("id") UUID id,
                                                                                                    @Valid @RequestBody DailyDietLogDto dto,
                                                                                                    @AuthenticationPrincipal UserPrincipal userPrincipal)  {
        service.updateDailyDietLog(id, dto, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

}
