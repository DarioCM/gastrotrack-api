package dev.dario.gastrotrackapi.dailyDietLog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DailyDietLogDto {

    private UUID id;
    @NotNull
    private UUID userId;

    @NotNull
    private String date;

    @NotNull
    private String meals;

    @NotNull
    private String typeMeal;


    private String notes;


}
