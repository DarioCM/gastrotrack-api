package dev.dario.gastrotrackapi.users.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;
    private String email;
    private String password;
    private Integer age;

    private String gender;

    private Double height; // Stored in meters

    private Double weight; // Stored in kilograms

    @Column(name = "activity_level")
    private String activityLevel; // e.g., "Sedentary", "Moderately Active"

    private String nationality;

    @Column(name = "diet_type")
    private String dietType; // e.g., "Vegetarian", "Meat-based"

    @Column(name = "gastritis_duration")
    private Period gastritisDuration; // Period to track duration

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
