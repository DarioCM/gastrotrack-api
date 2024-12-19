package dev.dario.gastrotrackapi.jpa.repository;

import dev.dario.gastrotrackapi.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
