package dev.dario.gastrotrackapi.dailydietlog;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DailyDietLogMapper {

  private final ModelMapper mapper;

  //  Convert entity to DTO
  public DailyDietLogDto convertToDto(DailyDietLogEntity entity) {
    return mapper.map(entity, DailyDietLogDto.class);
  }

  //  Convert DTO to entity
  public DailyDietLogEntity convertToEntity(DailyDietLogDto dto) {
    var entity = mapper.map(dto, DailyDietLogEntity.class);
    entity.setDate(LocalDate.parse(dto.getDate()));
    return entity;
  }


}
