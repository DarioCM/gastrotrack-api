package dev.dario.gastrotrackapi.jwt.models;

import dev.dario.gastrotrackapi.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class UserPrincipal implements UserDetails {

    private final UserEntity userEntity;

    public UserPrincipal(UserEntity user) {
        this.userEntity = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name()));  // Ensure "ROLE_USER"
    }

    @Override
    public String getPassword() {
        // Return the stored hashed password
        return new String(userEntity.getStoredHash());
    }

    @Override
    public String getUsername() {
        // Use the email as the username
        return this.userEntity.getEmail();
    }

    // return the UUID of the user
    public UUID getId() {
        return this.userEntity.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // For simplicity, return true. Add expiration logic if needed.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // For simplicity, return true. Add locking logic if needed.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // For simplicity, return true. Add expiration logic for credentials if needed.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Return true if the user is active. Modify based on your logic.
        return true;
    }

    //ROLE

}
