package dev.dario.gastrotrackapi.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/api/v1/users")
    public ResponseEntity<Iterable<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") UUID id) {
     return ResponseEntity.ok(userService.findUserById(id));
    }

    @DeleteMapping("/api/v1/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id) {
        userService.removeUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> postUser(
            @Valid @RequestBody UserDto userDto
    ) throws NoSuchAlgorithmException, BadRequestException {

        log.info("Creating user : {} ", userDto);

        var dto = userService.createUser(userDto, userDto.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


}
