package dev.dario.gastrotrackapi.foodSymptomLog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FoodSymptomLogDto {

        private UUID id;

        private UUID userId;

        @NotNull(message = "a food name is required")
        private String foodName;

        @NotNull(message = "at least one symptom is required")
        private String symptom;

        private Integer severity;

        private String notes;

        private LocalDateTime createAt;



}



