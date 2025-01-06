package dev.dario.gastrotrackapi.jwt.services;


import dev.dario.gastrotrackapi.jwt.models.UserPrincipal;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import dev.dario.gastrotrackapi.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@AllArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserPrincipal(userService.getUserByEmail(email));
    }

    private Boolean verifyPasswordHash(String password, byte[] storedHash, byte[] storedSalt) throws NoSuchAlgorithmException {

        var md = MessageDigest.getInstance("SHA-512");
        md.update(storedSalt);
        var computedHash = md.digest(password.getBytes());

        for (int i = 0; i < computedHash.length; i++) {
            if (computedHash[i] != storedHash[i]) {
                return false;
            }
        }

        return MessageDigest.isEqual(computedHash, storedHash);
    }

    public UserEntity authenticate(String email, String password)
        throws NoSuchAlgorithmException {

        if (email.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Unauthorized");
        }

        var userEntity = userService.getUserByEmail(email);

        if (userEntity == null)
            throw new BadCredentialsException("Unauthorized");

        var verified = verifyPasswordHash(
                password,
                userEntity.getStoredHash(),
                userEntity.getStoredSalt()
        );

        if (!verified)
            throw new BadCredentialsException("Unauthorized");

        return userEntity;
    }
}
