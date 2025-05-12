package dev.dario.gastrotrackapi.user;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
//@ActiveProfiles("test")
public class UsersControllerWithTestContainerTest {

  @Container
  @ServiceConnection
  private static PostgreSQLContainer
      postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

  @LocalServerPort
  private int port;

  @BeforeAll
   void setUp(){
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  @DisplayName("created user test")
  void testCreateUser_whenValidDetailsProvided_returnsCreateUser() {
    //arrange
    UserDto testUser = UserDto.builder()
        .age(30)
        .email("test@example.com")
        .gastritisDuration("6 months")
        .gender("Male")
        .height(1.75)
        .name("Test User")
        .weight(70.0)
        .password("securePassword123")
        .build();
    //act
    UserDto createdUSER =
        given().log().all()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(testUser)
    .when()
        .post("/register")
    .then()
        .extract().as(UserDto.class);
    //assert
    assertEquals(testUser.getName(), createdUSER.getName());
    assertNotNull(createdUSER.getId());

  }

}
