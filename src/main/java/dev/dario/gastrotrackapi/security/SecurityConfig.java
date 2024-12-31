package dev.dario.gastrotrackapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain
        securityFilterChain(HttpSecurity http) throws Exception {

        http
                // disable CSRF -> for stateless REST APIs
                .csrf(csrf -> csrf.disable())

                // Allow all requests to any endpoint (can be customized later)
                .authorizeHttpRequests(
                        authorize -> authorize.anyRequest().permitAll())

                // set session managment to sateless (for REST APIs)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                );

        return http.build();
    }

}
