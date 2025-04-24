package dev.dario.gastrotrackapi.dailydietlog;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DailyDietLogService {


  Page<DailyDietLogDto> findAllByUserId(UUID userId, Pageable pageable);

  DailyDietLogDto addDailyDietLog(DailyDietLogDto dto, UUID userId);

  void removeDailyDietLog(UUID id, UUID userId);

  void updateDailyDietLog(UUID id, DailyDietLogDto dto, UUID userId);

  void verifyOwnership(UUID id, UUID userId);

}
