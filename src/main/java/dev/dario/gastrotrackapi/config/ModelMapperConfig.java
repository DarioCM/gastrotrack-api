package dev.dario.gastrotrackapi.config;

import dev.dario.gastrotrackapi.dailyDietLog.dto.DailyDietLogDto;
import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }


}
