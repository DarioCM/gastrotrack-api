package dev.dario.gastrotrackapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    CorsFilter corsFilter() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        configuration.setAllowedHeaders(
                Arrays.asList(
                        "Origin",
                        "Access-Control-Allow-Origin",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "Origin, Accept",
                        "X-Requested-With",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
        );

        configuration.setExposedHeaders(
                Arrays.asList(
                        "Origin",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials"
                )
        );

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET", "POST", "PUT", "DELETE", "OPTIONS"
                )
        );

        var urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration(
                "/**",
                configuration
        );

        return new CorsFilter(urlBasedCorsConfigurationSource);

    }

    /*
    * The selected code defines a Spring Bean for configuring CORS (Cross-Origin Resource Sharing) in a Spring Boot application.

1. **Bean Definition**:
   ```java
   @Bean
   CorsFilter corsFilter() {
   ```
   This method is annotated with `@Bean`, indicating that it returns a Spring Bean to be managed by the Spring container.

2. **CORS Configuration**:
   ```java
   CorsConfiguration configuration = new CorsConfiguration();
   ```
   A new `CorsConfiguration` object is created to hold the CORS settings.

3. **Allow Credentials**:
   ```java
   configuration.setAllowCredentials(true);
   ```
   This allows cookies and other credentials to be included in CORS requests.

4. **Allowed Origins**:
   ```java
   configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
   ```
   Specifies the allowed origins for CORS requests. Here, only requests from `http://localhost:4200` are allowed.

5. **Allowed Headers**:
   ```java
   configuration.setAllowedHeaders(Arrays.asList(
       "Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept",
       "Authorization", "Origin, Accept", "X-Requested-With",
       "Access-Control-Request-Method", "Access-Control-Request-Headers"
   ));
   ```
   Lists the headers that can be used in CORS requests.

6. **Exposed Headers**:
   ```java
   configuration.setExposedHeaders(Arrays.asList(
       "Origin", "Content-Type", "Accept", "Authorization",
       "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
       "Access-Control-Allow-Credentials"
   ));
   ```
   Specifies which headers are exposed to the client.

7. **Allowed Methods**:
   ```java
   configuration.setAllowedMethods(Arrays.asList(
       "GET", "POST", "PUT", "DELETE", "OPTIONS"
   ));
   ```
   Lists the HTTP methods allowed for CORS requests.

8. **Register Configuration**:
   ```java
   var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
   urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
   ```
   Registers the CORS configuration for all URL patterns.

9. **Return CorsFilter**:
   ```java
   return new CorsFilter(urlBasedCorsConfigurationSource);
   ```
   Returns a new `CorsFilter` instance with the configured CORS settings.
    * */

}
