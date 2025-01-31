package dev.dario.gastrotrackapi.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String name; // Changed from username to name

    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @Lob
    @Column(nullable = false)
    private byte[] storedHash;

    @Lob
    @Column(nullable = false)
    private byte[] storedSalt;

    private Integer age;

    private String gender;

    private Double height; // Stored in meters

    private Double weight; // Stored in kilograms

    @Column(name = "gastritis_duration")
    private String gastritisDuration; // Duration information

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //adding a role to the user
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

}
