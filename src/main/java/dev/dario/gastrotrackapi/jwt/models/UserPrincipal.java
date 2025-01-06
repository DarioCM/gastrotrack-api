package dev.dario.gastrotrackapi.jwt.models;

import dev.dario.gastrotrackapi.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If roles or permissions are added in the future, map them here.
        // For now, return an empty list (or roles from the UserEntity).
        return Collections.emptyList();
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
}
