package dev.dario.gastrotrackapi.user.service;

import dev.dario.gastrotrackapi.exception.NotFoundException;
import dev.dario.gastrotrackapi.jpa.repository.UserRepository;
import dev.dario.gastrotrackapi.user.dto.UserDto;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository repository;
    private final ModelMapper mapper;

    private UserDto convertToDto(UserEntity entity) {
        return mapper.map(entity, UserDto.class);
    }

    private UserEntity convertToEntity(UserDto dto) {
        return mapper.map(dto, UserEntity.class);
    }

    // getting all users
    public List<UserDto> findAllUsers() {
        var userEntityList =
                new ArrayList<>( repository.findAll() );
        return userEntityList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // getting users by ID
    @Transactional(readOnly = true)
    public UserDto findUserById(final UUID id) {
        var user = repository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                "User by id " + id + " was not found")
                );
        return convertToDto(user);
    }

    // retrives a user by their email
    @Transactional(readOnly = true)
    public UserEntity getUserByEmail(String email) {
        var userEntity =
                Optional.ofNullable(
                        repository.findByEmail(email))
                .orElseThrow(() -> new NotFoundException(
                        "User by email " + email + " was not found"));
        return userEntity;
    }

    //creating a new User

    // create salt
    private byte[] createSalt(){
        var random = new SecureRandom();
        var salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // hash the user password
    private byte[] createPasswordHash(String password, byte[] salt) {
        try {
            var md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hashing algorithm not found", e);
        }
    }

    // create user
    @Transactional
    public UserDto createUser(UserDto userDto, String password)
            throws NoSuchAlgorithmException, BadRequestException {

        var user = convertToEntity(userDto);

        if (password.isBlank()) throw new
                IllegalArgumentException("Password is required.");

        var existsEmail = repository.selectExistsEmail(user.getEmail());

        if (existsEmail) throw new BadRequestException("Email " + user.getEmail() + "taken");

        // In cryptography, a salt is a random value added to the input of a hash function to ensure that the output (the hash) is unique even for identical inputs.
        byte[] salt = createSalt();

        // The hash in this context refers to the result of applying a hash function to the user's password combined with the salt.
        byte[] hashedPassword = createPasswordHash(password, salt);

        user.setStoredSalt(salt);
        user.setStoredHash(hashedPassword);

        log.info("Creating user Entity > : {} ", user);

        repository.save(user);

        return convertToDto(user);
    }

    //update user
    // update user
    @Transactional
    public void updateUser(UUID id, UserDto userDto, String password) {
        var user = findOrThrow(id);
        var userParam = convertToEntity(userDto);

        user.setName(userParam.getName());
        user.setEmail(userParam.getEmail());
        user.setAge(userParam.getAge());
        user.setGender(userParam.getGender());
        user.setHeight(userParam.getHeight());
        user.setWeight(userParam.getWeight());
        user.setGastritisDuration(userParam.getGastritisDuration());

        if (password != null && !password.isBlank()) {
            byte[] salt = createSalt();
            byte[] hashedPassword = createPasswordHash(password, salt);
            user.setStoredSalt(salt);
            user.setStoredHash(hashedPassword);
        }

        repository.save(user);
    }


    // remove user by id
    @Transactional
    public void removeUserById(UUID id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    private UserEntity findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User by id " + id + " was not found"));
    }


}
