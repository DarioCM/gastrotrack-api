package dev.dario.gastrotrackapi.users.record;

import jakarta.validation.constraints.*;
import java.time.Period;
import java.util.UUID;

public record UserRecord(
        UUID id,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age cannot be negative")
        Integer age,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotNull(message = "Height is required")
        @DecimalMin(value = "0.5", inclusive = true, message = "Height must be at least 0.5 meters")
        @DecimalMax(value = "3.0", inclusive = true, message = "Height must be less than 3.0 meters")
        Double height,

        @NotNull(message = "Weight is required")
        @DecimalMin(value = "10.0", inclusive = true, message = "Weight must be at least 10 kg")
        @DecimalMax(value = "500.0", inclusive = true, message = "Weight must be less than 500 kg")
        Double weight,

        @NotBlank(message = "Activity level is required")
        String activityLevel,

        @NotBlank(message = "Nationality is required")
        String nationality,

        @NotBlank(message = "Diet type is required")
        String dietType,

        @NotNull(message = "Gastritis duration is required")
        Period gastritisDuration

) {
}
