package dev.dario.gastrotrackapi.auth;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final ApplicationUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ Fix: Enable CORS at the security level
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ✅ Disable CSRF (recommended for stateless APIs)
            .csrf(csrf -> csrf.disable())

            // ✅ Allow authentication requests, restrict others
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/authenticate", "/register").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Admin access
                .requestMatchers("/api/v1/**").hasRole("USER") // User access
                .anyRequest().authenticated()
            )

            // ✅ Set session management to stateless (for REST APIs)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ✅ Add JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ✅ Fix: Define CORS Configuration Source
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);

        // ✅ Allow React frontend
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:5173"));

        // ✅ Allow common HTTP headers
        configuration.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));

        // ✅ Allow standard HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Allow credentials (JWT tokens)
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}