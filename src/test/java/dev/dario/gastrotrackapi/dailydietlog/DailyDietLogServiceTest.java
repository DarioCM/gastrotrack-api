package dev.dario.gastrotrackapi.dailydietlog;

import dev.dario.gastrotrackapi.dailydietlog.dto.DailyDietLogDto;
import dev.dario.gastrotrackapi.dailydietlog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.dailydietlog.service.DailyDietLogService;
import dev.dario.gastrotrackapi.exception.NotFoundException;
import dev.dario.gastrotrackapi.jpa.repository.DailyDietLogRepository;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DailyDietLogServiceTest {

    @Mock
    private DailyDietLogRepository dailyDietLogRepository;

    @Mock
    private ModelMapper modelMapper;

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

        when(modelMapper.map(any(DailyDietLogEntity.class), eq(DailyDietLogDto.class))).thenAnswer(invocation -> {
            DailyDietLogEntity entity = invocation.getArgument(0);
            DailyDietLogDto dto = new DailyDietLogDto();
            dto.setId(entity.getId());
            dto.setDate(entity.getDate().toString());
            dto.setMeals(entity.getMeals());
            dto.setUserId(testUser.getId());
            return dto;
        });

        when(modelMapper.map(any(DailyDietLogDto.class), eq(DailyDietLogEntity.class))).thenAnswer(invocation -> {
            DailyDietLogEntity entity = new DailyDietLogEntity();
            entity.setId(testLog.getId());
            entity.setDate(testLog.getDate());
            entity.setMeals(testLog.getMeals());
            UserEntity user = new UserEntity();
            user.setId(testUser.getId());
            entity.setUser(testUser);
            return entity;
        });
    }

    @Test
    void testFindAllByUserId() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").descending());

        Page<DailyDietLogEntity> mockPage = new PageImpl<>(List.of(testLog), pageable, 1);
        when(dailyDietLogRepository.findAllByUserId(eq(testUser.getId()), any(Pageable.class))).thenReturn(mockPage);

        Page<DailyDietLogDto> logs = dailyDietLogService.findAllByUserId(testUser.getId(), pageable);

        assertThat(logs).isNotEmpty();
        assertThat(logs.getContent().get(0).getMeals()).isEqualTo("Lunch");

    }

    @Test
    void testAddDailyDietLog() {
        when(dailyDietLogRepository.save(any(DailyDietLogEntity.class))).thenReturn(testLog);

        DailyDietLogDto dto = dailyDietLogService.convertToDto(testLog);
        DailyDietLogDto savedLog = dailyDietLogService.addDailyDietLog(dto, testUser.getId());

        assertThat(savedLog.getMeals()).isEqualTo("Lunch");
        verify(dailyDietLogRepository, times(1)).save(any(DailyDietLogEntity.class));
    }

    @Test
    void testRemoveDailyDietLog() {
        doNothing().when(dailyDietLogRepository).deleteById(testLog.getId());
        when(dailyDietLogRepository.findById(testLog.getId())).thenReturn(Optional.of(testLog));

        dailyDietLogService.removeDailyDietLog(testLog.getId(), testUser.getId());

        verify(dailyDietLogRepository, times(1)).deleteById(testLog.getId());
    }

    @Test
    void testUpdateDailyDietLog() {
        when(dailyDietLogRepository.findById(testLog.getId())).thenReturn(Optional.of(testLog));
        when(dailyDietLogRepository.save(any(DailyDietLogEntity.class))).thenReturn(testLog);

        testLog.setMeals("Updated Lunch");
        DailyDietLogDto dto = dailyDietLogService.convertToDto(testLog);
        dailyDietLogService.updateDailyDietLog(testLog.getId(), dto , testUser.getId());

        verify(dailyDietLogRepository, times(1)).save(any(DailyDietLogEntity.class));
        assertThat(dto.getMeals()).isEqualTo("Updated Lunch");
    }

    @Test
    void testRemoveDailyDietLog_NotFound() {
        when(dailyDietLogRepository.findById(testLog.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dailyDietLogService.removeDailyDietLog(testLog.getId(), testUser.getId()))
                .isInstanceOf(NotFoundException.class);

        verify(dailyDietLogRepository, never()).deleteById(testLog.getId());
    }

    @Test
    void testUpdateDailyDietLog_NotFound() {
        when(dailyDietLogRepository.findById(testLog.getId())).thenReturn(Optional.empty());

        DailyDietLogDto dto = dailyDietLogService.convertToDto(testLog);
        assertThatThrownBy(() -> dailyDietLogService.updateDailyDietLog(testLog.getId(), dto, testUser.getId()))
                .isInstanceOf(NotFoundException.class);

        verify(dailyDietLogRepository, never()).save(testLog);
    }
}
