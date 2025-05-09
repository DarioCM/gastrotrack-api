package dev.dario.gastrotrackapi.dailydietlog;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DailyDietLogControllerTestContainerTest {

  @Container
  @ServiceConnection
  private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(
      "postgres:latest");

  @Test
  @DisplayName("The test container is created")
  void testContainerIsrunning(){
    assertTrue(postgreSQLContainer.isCreated(), "Container noty created yet");
    assertTrue(postgreSQLContainer.isRunning(), "Container is not running");
  }

}
