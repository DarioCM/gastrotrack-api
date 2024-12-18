package dev.dario.gastrotrackapi.users.service;

import dev.dario.gastrotrackapi.exception.NotFoundException;
import dev.dario.gastrotrackapi.users.entity.UserEntity;
import dev.dario.gastrotrackapi.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository repository;

    // create
    public UserEntity createUser(UserEntity entity) {
        return repository.save(entity);
    }

    //update
    public void updateUser(UUID id, UserEntity entity) {
        findOrThrow(id);
        repository.save(entity);
    }

    // findOrThrow
    private UserEntity findOrThrow(final UUID id) {
        return repository
                .findById(id)
                .orElseThrow( () -> new NotFoundException("User not found"));
    }



}
