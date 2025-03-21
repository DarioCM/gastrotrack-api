package dev.dario.gastrotrackapi.foodsymptomlog;


import dev.dario.gastrotrackapi.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "food_symptom_logs")
public class FoodSymptomLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String foodName;
    private String symptom;

    private Integer severity;

    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();



}
