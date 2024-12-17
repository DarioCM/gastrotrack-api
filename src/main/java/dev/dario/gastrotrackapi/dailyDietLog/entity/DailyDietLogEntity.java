package dev.dario.gastrotrackapi.dailyDietLog.entity;


import dev.dario.gastrotrackapi.users.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "daily_diet_logs")
public class DailyDietLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate date;

    @Column(columnDefinition = "json", nullable = false)
    private String meals; // JSON format for meals

    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}
