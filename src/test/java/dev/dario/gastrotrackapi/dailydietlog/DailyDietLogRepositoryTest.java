package dev.dario.gastrotrackapi.dailydietlog;


import dev.dario.gastrotrackapi.dailydietlog.entity.DailyDietLogEntity;
import dev.dario.gastrotrackapi.jpa.repository.DailyDietLogRepository;
import dev.dario.gastrotrackapi.jpa.repository.UserRepository;
import dev.dario.gastrotrackapi.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;



@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DailyDietLogRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    DailyDietLogRepository repository;

    @Autowired
    UserRepository userRepository;

    private UserEntity testUser;
    private DailyDietLogEntity log;

    @BeforeEach
    @Transactional
    void setUp() throws NoSuchAlgorithmException {
        // Create a test user
        testUser = new UserEntity();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        // Create a dummy salt and hash for testing
        byte[] dummySalt = "dummySalt".getBytes(); // Replace with real logic if necessary
        byte[] dummyHash = "dummyHash".getBytes(); // Replace with real logic if necessary
        testUser.setStoredSalt(dummySalt);
        testUser.setStoredHash(dummyHash);

        // Save the user
        userRepository.save(testUser);

        // Create and save a DailyDietLogEntity
        log = new DailyDietLogEntity();
        log.setDate(LocalDate.now());
        log.setMeals("Breakfast");
        log.setNotes("Test notes");
        log.setUser(testUser);

        repository.save(log);
    }

    @Test
    void testFindLogsByUser() {
        List<DailyDietLogEntity> logs = repository.findAllByUserId(testUser.getId(), PageRequest.of(0, 10)).getContent();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getMeals()).isEqualTo("Breakfast");
    }

    @Test
    void testFindLogsByDate() {
        List<DailyDietLogEntity> logs = repository.findAllByUserId(testUser.getId(), PageRequest.of(0, 10)).getContent();
        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void testUpdateLog() {
        log.setMeals("Updated Breakfast");
        repository.save(log);

        Optional<DailyDietLogEntity> updatedLog = repository.findById(log.getId());
        assertThat(updatedLog).isPresent();
        assertThat(updatedLog.get().getMeals()).isEqualTo("Updated Breakfast");
    }


    @Test
    void testDeleteLog() {
        repository.deleteById(log.getId());

        Optional<DailyDietLogEntity> deletedLog = repository.findById(log.getId());
        assertThat(deletedLog).isNotPresent();
    }


}
