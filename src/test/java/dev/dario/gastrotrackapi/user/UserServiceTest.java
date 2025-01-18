package dev.dario.gastrotrackapi.user;


import dev.dario.gastrotrackapi.exception.NotFoundException;
import dev.dario.gastrotrackapi.jpa.repository.UserRepository;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import dev.dario.gastrotrackapi.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserEntity();
        testUser.setId(UUID.randomUUID());
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setStoredSalt("dummySalt".getBytes());
        testUser.setStoredHash("dummyHash".getBytes());
    }

    @Test
    void testGetUserByEmail() {
        when(repository.findByEmail(testUser.getEmail())).thenReturn(testUser);

        UserEntity user = service.getUserByEmail(testUser.getEmail());

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        verify(repository, times(1)).findByEmail(testUser.getEmail());
    }


    @Test
    void testGetUserByEmailNotFound() {
        when(repository.findByEmail(testUser.getEmail())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            service.getUserByEmail(testUser.getEmail());
        });

        verify(repository, times(1)).findByEmail(testUser.getEmail());
    }






}
