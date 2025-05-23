package dev.dario.gastrotrackapi.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto  {

        private UUID id;

        private String name;

        private String password;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age cannot be negative")
        private Integer age;

        @NotBlank(message = "Gender is required")
        private String gender;

        @NotNull(message = "Height is required")
        @DecimalMin(value = "0.5", inclusive = true, message = "Height must be at least 0.5 meters")
        @DecimalMax(value = "3.0", inclusive = true, message = "Height must be less than 3.0 meters")
        private Double height;

        @NotNull(message = "Weight is required")
        @DecimalMin(value = "10.0", inclusive = true, message = "Weight must be at least 10 kg")
        @DecimalMax(value = "500.0", inclusive = true, message = "Weight must be less than 500 kg")
        private Double weight;


        @NotNull(message = "Gastritis duration is required")
        private String gastritisDuration;


}
