package dev.dario.gastrotrackapi.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "dev.dario.gastrotrackapi.jpa.repository")
public class JpaConfig {
}
