package dev.dario.gastrotrackapi.auth;

import dev.dario.gastrotrackapi.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthenticateController {

    private final JwtUtil jwtTokenUtil;

    private final ApplicationUserDetailsService userDetailsService;


    @RequestMapping(value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
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
        log.info("Authenticated :" + userDetails.toString());

        var jwt = jwtTokenUtil.generateToken(userDetails);
        log.info("JWT : " + jwt);

        return new AuthenticationResponse(jwt);

    }

    // logOut() method
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token ) {
       jwtTokenUtil.invalidateToken(token);
         return ResponseEntity.ok().build();
    }



}
