package dev.dario.gastrotrackapi.auth;

import dev.dario.gastrotrackapi.user.UserEntity;
import dev.dario.gastrotrackapi.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ApplicationUserDetailsService service;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail("test@example.com");
        userEntity.setStoredHash(new byte[]{1, 2, 3});
        userEntity.setStoredSalt(new byte[]{4, 5, 6});
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(userEntity);

        var userDetails = service.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(userService.getUserByEmail("test@example.com")).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("test@example.com"));
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }



    @Test
    void authenticate_UserDoesNotExist_ThrowsBadCredentialsException() {
        when(userService.getUserByEmail("test@example.com")).thenThrow(new BadCredentialsException("User not found"));

        assertThrows(BadCredentialsException.class, () -> service.authenticate("test@example.com", "password"));
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }
}
