package dev.dario.gastrotrackapi.dailyDietLog.record;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record DailyDietLogRecord(
        UUID id,
        UUID userId,
        LocalDate date,
        @NotNull(message = "Meals description is required")
        String meals,
        String notes
) {
}
