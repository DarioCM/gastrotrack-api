package dev.dario.gastrotrackapi.dailyDietLog;

import dev.dario.gastrotrackapi.dailyDietLog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.dailyDietLog.service.DailyDietLogService;
import dev.dario.gastrotrackapi.jpa.repository.DailyDietLogRepository;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DailyDietLogServiceTest {

    @Mock
    private DailyDietLogRepository dailyDietLogRepository;

    @InjectMocks
    private DailyDietLogService dailyDietLogService;

    private UserEntity testUser;
    private DailyDietLogEntity testLog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserEntity();
        testUser.setId(UUID.randomUUID());
        testUser.setName("Test User");

        testLog = new DailyDietLogEntity();
        testLog.setId(UUID.randomUUID());
        testLog.setDate(LocalDate.now());
        testLog.setMeals("Lunch");
        testLog.setUser(testUser);
    }

    @Test
    void testFindAllByUserId() {
        when(dailyDietLogRepository.findAllByUserId(testUser.getId())).thenReturn(List.of(testLog));

        List<DailyDietLogEntity> logs = dailyDietLogService.findAllByUserId(testUser.getId());

        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getMeals()).isEqualTo("Lunch");
        verify(dailyDietLogRepository, times(1)).findAllByUserId(testUser.getId());
    }

    @Test
    void testAddDailyDietLog() {
        when(dailyDietLogRepository.save(any(DailyDietLogEntity.class))).thenReturn(testLog);

        DailyDietLogEntity savedLog = dailyDietLogService.addDailyDietLog(testLog);

        assertThat(savedLog.getMeals()).isEqualTo("Lunch");
        verify(dailyDietLogRepository, times(1)).save(testLog);
    }

}
