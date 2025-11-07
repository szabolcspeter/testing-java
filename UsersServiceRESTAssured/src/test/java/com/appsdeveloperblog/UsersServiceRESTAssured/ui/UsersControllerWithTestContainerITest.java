package com.appsdeveloperblog.UsersServiceRESTAssured.ui;

import com.appsdeveloperblog.UsersServiceRESTAssured.ui.model.User;
import com.appsdeveloperblog.UsersServiceRESTAssured.ui.model.UserRest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class UsersControllerWithTestContainerITest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0");

    @LocalServerPort
    private int port;

    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_PASSWORD = "123456789";
    private String userId;
    private String token;

    // This was we log only Body and Headers and not else
//    private final RequestLoggingFilter requestLoggingFilter = RequestLoggingFilter.with(LogDetail.BODY, LogDetail.HEADERS);
    // This way we log everything
    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @BeforeAll
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(2000L))
                .build();
    }

    @Order(1)
    @Test
    void testContainerIsRunning() {
        assertTrue(mysqlContainer.isRunning());
    }

    @Order(2)
    @Test
    void testCreateUser_whenValidDetailsProvided_returnsCreatedUser() {

        // Arrange
        User newUser = new User(
                "Szabi",
                "Peter",
                TEST_EMAIL,
                TEST_PASSWORD
        );

        // Act
        // *** It uses Fluent API ***
        given() // setup HTTP details
                .body(newUser)
        .when() // used to specify HTTP method and API endpoint that we want to call
                .post("/users")
        .then() // we verify HTTP response
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", equalTo(newUser.getFirstName()))
                .body("lastName", equalTo(newUser.getLastName()))
                .body("email", equalTo(newUser.getEmail()));
    }

    @Order(3)
    @Test
    void testLogin_whenValidCredentialsProvided_returnsTokenAndUserIdHeaders() {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", TEST_EMAIL);
        credentials.put("password", TEST_PASSWORD);

        // Act
        Response response =
                given().body(credentials)
                .when().post("/login");

        userId = response.header("userId");
        token = response.header("token");

        // Assert
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(userId);
        assertNotNull(token);
    }
}
