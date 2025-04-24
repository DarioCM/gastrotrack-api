package dev.dario.gastrotrackapi.dailydietlog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dario.gastrotrackapi.auth.UserPrincipal;
import dev.dario.gastrotrackapi.user.UserEntity;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DailyDietLogController.class)
@AutoConfigureMockMvc(addFilters = false)   // we’ve deactivated filters in “test” profile
@ActiveProfiles("test")
class DailyDietLogControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockitoBean
  DailyDietLogService service;
  private final ObjectMapper om = new ObjectMapper();


  @Test
  void testAddDailyDietLog_withManualSecurityContext() throws Exception {
    // — arrange a real user entity & principal
    UUID userId = UUID.randomUUID();
    UserEntity ue = new UserEntity();
    ue.setId(userId);
    // if your UserPrincipal constructor takes a UserEntity:
    UserPrincipal principal = new UserPrincipal(ue);

    // — stub your service
    DailyDietLogDto saved = DailyDietLogDto.builder()
        .id(UUID.randomUUID())
        .userId(userId)
        .date("2025-04-25")
        .meals("Lunch")
        .typeMeal("MEAL")
        .notes("OK")
        .build();
    when(service.addDailyDietLog(any(), eq(userId))).thenReturn(saved);

    // — request payload
    DailyDietLogDto req = DailyDietLogDto.builder()
        .userId(userId)
        .date("2025-04-25")
        .meals("Lunch")
        .typeMeal("MEAL")
        .notes("OK")
        .build();
    String json = om.writeValueAsString(req);

    // —— **<<< THE KEY STEP >>>**
    // Seed SecurityContext so @AuthenticationPrincipal will be your UserPrincipal
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(
            principal,                  // principal object
            /* credentials= */ null,
            principal.getAuthorities()  // any authorities you need
        )
    );

    // — fire the request (no .with(...) or headers needed)
    mockMvc.perform(post("/api/v1/daily-diet-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(userId.toString()));
  }
}
