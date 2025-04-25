package dev.dario.gastrotrackapi.dailydietlog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dario.gastrotrackapi.auth.UserPrincipal;
import dev.dario.gastrotrackapi.user.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(DailyDietLogController.class)
@AutoConfigureMockMvc(addFilters = false) // to pass security filters
@ActiveProfiles("test")
class DailyDietLogControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  DailyDietLogService service;

  private final ObjectMapper om = new ObjectMapper();

  // for mock validation porpose
  UUID userId;
  UserEntity ue;
  UserPrincipal principal;

  @BeforeEach
  void setUp() {
    // — arrange a real user entity & principal
    userId = UUID.randomUUID();
    ue = new UserEntity();
    ue.setId(userId);
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

    MvcResult mvcResult = mockMvc.perform(post("/api/v1/daily-diet-logs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andReturn();

    String responseBodyAsString = mvcResult.getResponse().getContentAsString();

    DailyDietLogDto logRes = om.readValue(responseBodyAsString, DailyDietLogDto.class);

    Assertions.assertEquals(userId, logRes.getUserId());

  }

  @Test
  @DisplayName("getAllByUserID")
  void test_getAllByUserId() throws Exception {
    // pre
    List<DailyDietLogDto> logDtoList = new ArrayList<>();

    logDtoList.add(DailyDietLogDto.builder()
        .id(UUID.randomUUID())
        .userId(userId)
        .date("2025-04-25")
        .meals("Breakfast")
        .typeMeal("MEAL")
        .notes("Healthy breakfast")
        .build());

    logDtoList.add(DailyDietLogDto.builder()
        .id(UUID.randomUUID())
        .userId(userId)
        .date("2025-04-26")
        .meals("Lunch")
        .typeMeal("MEAL")
        .notes("Light lunch")
        .build());

    logDtoList.add(DailyDietLogDto.builder()
        .id(UUID.randomUUID())
        .userId(userId)
        .date("2025-04-27")
        .meals("Dinner")
        .typeMeal("MEAL")
        .notes("Delicious dinner")
        .build());

    // Create a Pageable instance
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));
    Page<DailyDietLogDto> page = new PageImpl<>(logDtoList, pageable, logDtoList.size());
    when(service.findAllByUserId(eq(userId), any(Pageable.class))).thenReturn(page);

    // action
    mockMvc.perform(get("/api/v1/daily-diet-logs")
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(logDtoList.size()))
        .andExpect(jsonPath("$.content[0].meals").value("Breakfast"));

  }

  //test 400
  @Test
  @DisplayName("400 creating log")
  void test_addLog_missingFields_thenBadRequest() throws Exception {
    //empty DTO
    String json = "{}";
    mockMvc.perform(
            post("/api/v1/daily-diet-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").exists());
  }


  @Test
  @DisplayName("DELETE /api/v1/daily-diet-logs/{id} → 204 No Content")
  void testRemoveDailyDietLog_success() throws Exception {
    UUID logId = UUID.randomUUID();
    // stub service to do nothing
    doNothing().when(service).removeDailyDietLog(eq(logId), eq(userId));

    mockMvc.perform(delete("/api/v1/daily-diet-logs/{id}", logId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("PUT /api/v1/daily-diet-logs/{id} → 204 No Content")
  void testUpdateDailyDietLog_success() throws Exception {
    UUID logId = UUID.randomUUID();
    // build update DTO (id not required in body since path provides it)
    DailyDietLogDto dto = DailyDietLogDto.builder()
        .userId(userId)
        .date("2025-04-28")
        .meals("Snack")
        .typeMeal("MEAL")
        .notes("Updated note")
        .build();
    String json = om.writeValueAsString(dto);

    // stub service update
    doNothing().when(service).updateDailyDietLog(eq(logId), any(DailyDietLogDto.class), eq(userId));

    mockMvc.perform(put("/api/v1/daily-diet-logs/{id}", logId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNoContent());
  }
}
