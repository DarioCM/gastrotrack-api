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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class DailyDietLogControllerTestContainerSTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  UserEntity ue;
  UserPrincipal principal;

  @Container
  @ServiceConnection
  private static final PostgreSQLContainer
      postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

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
