package dev.dario.gastrotrackapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GastrotrackApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GastrotrackApiApplication.class, args);
    }

}
