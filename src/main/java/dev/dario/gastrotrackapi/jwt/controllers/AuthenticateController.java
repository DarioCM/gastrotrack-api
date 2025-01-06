package dev.dario.gastrotrackapi.jwt.controllers;

import dev.dario.gastrotrackapi.jwt.models.AuthenticationRequest;
import dev.dario.gastrotrackapi.jwt.models.AuthenticationResponse;
import dev.dario.gastrotrackapi.jwt.services.ApplicationUserDetailsService;
import dev.dario.gastrotrackapi.jwt.util.JwtUtil;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtTokenUtil;

    private final ApplicationUserDetailsService userDetailsService;


    @RequestMapping(value = "/authenticate")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticationRequest req
    ) throws Exception {

        UserEntity user;
        try {
            user = userDetailsService.authenticate(
                    req.getEmail(), req.getPassword()
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        var userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());
        log.info(userDetails.toString());

        var jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);


    }


}
