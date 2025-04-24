package dev.dario.gastrotrackapi.dailydietlog;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DailyDietLogDto {

    private UUID id;


    private UUID userId;

    @NotNull
    private String date;

    @NotNull
    private String meals;

    @NotNull
    private String typeMeal;


    private String notes;


    // Public no-argument constructor
    public DailyDietLogDto() {
    }

    // Parameterized constructor (optional)
    public DailyDietLogDto(UUID id, String date, String meals, String typeMeal, String notes, String userId) {
        this.id = id;
        this.date = date;
        this.meals = meals;
        this.typeMeal = typeMeal;
        this.notes = notes;
        this.userId = UUID.fromString(userId);
    }

    // After
    public DailyDietLogDto(UUID id, UUID userId, String date, String meals, String typeMeal, String notes) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.meals = meals;
        this.typeMeal = typeMeal;
        this.notes = notes;
    }


}
