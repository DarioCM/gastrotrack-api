package dev.dario.gastrotrackapi.security;

import dev.dario.gastrotrackapi.jwt.filters.JwtRequestFilter;
import dev.dario.gastrotrackapi.jwt.services.ApplicationUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final ApplicationUserDetailsService userDetailsService;

    private final JwtRequestFilter jwtFilter;

    @Bean
    public SecurityFilterChain
        securityFilterChain(HttpSecurity http) throws Exception {

        http
                // disable CSRF -> for stateless REST APIs
                .csrf(csrf -> csrf.disable())

                // Authorize requests
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/authenticate").permitAll() // Allow public access to /authenticate
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )

                // set session managment to sateless (for REST APIs)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

               // Add JWT filter before UsernamePasswordAuthenticationFilter
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Expose AuthenticationManager as a bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
    @Bean
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    */
}
