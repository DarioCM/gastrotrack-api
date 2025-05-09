package dev.dario.gastrotrackapi.dailydietlog;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DailyDietLogControllerTestContainerTest {

  @Container
  private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(
      "postgres:latest")
      .withDatabaseName("gastrotrackdb")
      .withUsername("postgres")
      .withPassword("password");

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry){
    registry.add("spring.data.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.data.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.data.datasource.password", postgreSQLContainer::getPassword);
  }

  @Test
  @DisplayName("The test container is created")
  void testContainerIsrunning(){
    assertTrue(postgreSQLContainer.isCreated(), "Container noty created yet");
    assertTrue(postgreSQLContainer.isRunning(), "Container is not running");
  }






}
