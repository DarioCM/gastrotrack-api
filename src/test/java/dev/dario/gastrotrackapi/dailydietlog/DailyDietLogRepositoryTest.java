package dev.dario.gastrotrackapi.dailydietlog;

import static org.assertj.core.api.Assertions.assertThat;

import dev.dario.gastrotrackapi.user.UserEntity;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class DailyDietLogRepositoryTest {

  @Autowired
  private DailyDietLogRepository repository;

  @Autowired
  private TestEntityManager entityManager;


  @Test
  void testSaveLog() {
    UserEntity userEntity = new UserEntity();
    userEntity.setName("test");
    userEntity.setEmail("test@test.com");
    UserEntity managedUser = entityManager.persist(userEntity);
    UUID userId = managedUser.getId();

    DailyDietLogEntity logEntity = new DailyDietLogEntity();
    logEntity.setUser(managedUser);
    logEntity.setDate(LocalDate.now());
    logEntity.setMeals("Lunch");
    logEntity.setTypeMeal("MEAL");
    logEntity.setNotes("Sample note");
    DailyDietLogEntity saveEntity = repository.save(logEntity);

    assertThat(saveEntity.getUser().getId()).isEqualTo(userId);

  }

}