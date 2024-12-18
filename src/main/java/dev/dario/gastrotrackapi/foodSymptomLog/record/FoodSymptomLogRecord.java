package dev.dario.gastrotrackapi.foodSymptomLog.record;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record FoodSymptomLogRecord(
        UUID id,
        UUID userId,
        @NotNull(message = "a food name is required")
        String foodName,
        @NotNull(message = "at least one symptom is required")
        String symptom,
        Integer severity,
        String notes,
        LocalDateTime createAt


) {
}
