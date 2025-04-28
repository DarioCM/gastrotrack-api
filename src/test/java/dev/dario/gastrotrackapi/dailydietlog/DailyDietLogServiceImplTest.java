package dev.dario.gastrotrackapi.dailydietlog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


import dev.dario.gastrotrackapi.user.UserEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class DailyDietLogServiceImplTest {

  @Mock
  private DailyDietLogRepository repository;

  @Mock
  private DailyDietLogMapper mapper; // because the service implementation user it

  @InjectMocks
  private DailyDietLogServiceImpl service;

  UserEntity user;

  @BeforeEach
  void setUp() {
    user = new UserEntity();
    user.setId(UUID.randomUUID());
    user.setEmail("test@test.com");
    user.setName("Name Test");

    // Mock mapper behavior
    when(mapper.convertToDto(any(DailyDietLogEntity.class))).thenAnswer(invocation -> {
      DailyDietLogEntity entity = invocation.getArgument(0);
      return DailyDietLogDto.builder()
          .date(entity.getDate().toString())
          .meals(entity.getMeals())
          .typeMeal(entity.getTypeMeal())
          .notes(entity.getNotes())
          .build();
    });
  }

  @Test
  void testfindAllByUserId_OK() {
    //
    DailyDietLogEntity logEntity = new DailyDietLogEntity();
    logEntity.setUser(user);
    logEntity.setDate(LocalDate.now());
    logEntity.setMeals("Lunch");
    logEntity.setTypeMeal("MEAL");
    logEntity.setNotes("Sample note");

    DailyDietLogEntity logEntity2 = new DailyDietLogEntity();
    logEntity2.setUser(user);
    logEntity2.setDate(LocalDate.now());
    logEntity2.setMeals("Lunch");
    logEntity2.setTypeMeal("MEAL");
    logEntity2.setNotes("Sample note");

    Pageable pageable = PageRequest.of(0, 10);
    List<DailyDietLogEntity> list = List.of(logEntity, logEntity2);
    Page<DailyDietLogEntity> page = new PageImpl<>(list, pageable, list.size());
    when(repository.findAllByUserId(eq(user.getId()), any(Pageable.class))).thenReturn(page);

    // action
    Page<DailyDietLogDto> resultPage = service.findAllByUserId(user.getId(), pageable);

    //verify
    assertEquals(list.size(), resultPage.stream().toList().size());
    // Verify the content of the result
    assertEquals(logEntity.getMeals(), resultPage.getContent().get(0).getMeals());
    assertEquals(logEntity2.getMeals(), resultPage.getContent().get(1).getMeals());


  }


}