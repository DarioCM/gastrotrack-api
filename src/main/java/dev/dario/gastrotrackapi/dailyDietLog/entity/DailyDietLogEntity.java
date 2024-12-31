package dev.dario.gastrotrackapi.dailyDietLog.entity;


import dev.dario.gastrotrackapi.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;



@Data
@Entity
@Table(name = "daily_diet_logs")
public class DailyDietLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Many-to-One relationship with UserEntity
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private LocalDate date;

    @Column(nullable = false)
    private String meals; // should be a string

    private String typeMeal;

    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "DailyDietLogEntity{" +
                "id=" + id +
                ", date=" + date +
                ", meals='" + meals + '\'' +
                ", typeMeal='" + typeMeal + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

}
