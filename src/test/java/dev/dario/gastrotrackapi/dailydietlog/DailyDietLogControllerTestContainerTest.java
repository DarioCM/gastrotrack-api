package dev.dario.gastrotrackapi.dailydietlog;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dario.gastrotrackapi.auth.UserPrincipal;
import dev.dario.gastrotrackapi.user.UserEntity;
import dev.dario.gastrotrackapi.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class DailyDietLogControllerTestContainerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  UserEntity ue;
  UserPrincipal principal;

  @Container
  private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(
      "postgres:latest")
      .withDatabaseName("gastrotrackdb")
      .withUsername("postgres")
      .withPassword("password");

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.data.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.data.datasource.password", postgreSQLContainer::getPassword);
  }

  @Test
  @DisplayName("The test container is created")
  void testContainerIsrunning() {
    assertTrue(postgreSQLContainer.isCreated(), "Container noty created yet");
    assertTrue(postgreSQLContainer.isRunning(), "Container is not running");
  }

  @BeforeEach
  void setUp() {
    // — arrange a real user entity & principal
    ue = new UserEntity();
    ue.setName("Test container tes");
    ue.setEmail("john@example.com");
    ue.setStoredHash("test-password".getBytes());
    ue.setStoredSalt("salt".getBytes());
    ue = userRepository.save(ue);
    // if your UserPrincipal constructor takes a UserEntity:
    principal = new UserPrincipal(ue);
    // —— **<<< THE KEY STEP >>>**
    // Seed SecurityContext so @AuthenticationPrincipal will be your UserPrincipal
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(
            principal,                  // principal object
            /* credentials= */ null,
            principal.getAuthorities()  // any authorities you need
        )
    );
  }

  @Test
  @DisplayName("Create log test")
  void testAddDailyDietLog_withManualSecurityContext() throws Exception {

    // — request payload
    DailyDietLogDto req = DailyDietLogDto.builder()
        .userId(ue.getId())
        .date("2025-04-25")
        .meals("Lunch")
        .typeMeal("MEAL")
        .notes("OK")
        .build();
    String json = objectMapper.writeValueAsString(req);

    MvcResult mvcResult = mockMvc.perform(post("/api/v1/daily-diet-logs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andReturn();

    String responseBodyAsString = mvcResult.getResponse().getContentAsString();

    DailyDietLogDto logRes = objectMapper.readValue(responseBodyAsString, DailyDietLogDto.class);

    Assertions.assertEquals(ue.getId(), logRes.getUserId());

  }

}
