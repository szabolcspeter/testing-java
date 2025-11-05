package com.appsdeveloperblog.UsersServiceRESTAssured.ui;

import com.appsdeveloperblog.UsersServiceRESTAssured.ui.model.User;
import com.appsdeveloperblog.UsersServiceRESTAssured.ui.model.UserRest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
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

    @BeforeAll
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
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
                "test@test.com",
                "123456789"
        );

        // Act
        // *** It uses Fluent API ***
        Response response = given() // setup HTTP details
                .contentType(ContentType.JSON) // longer syntax  .header("Content-Type", "application/json")
                .accept(ContentType.JSON) // longer syntax .header("Accept", "application/json")
                .body(newUser)
        .when() // used to specify HTTP method and API endpoint that we want to call
                .post("/users")
        .then() // we verify HTTP response
                .extract()
                .response();

        // Assert
        assertEquals(201, response.statusCode());
        assertEquals(newUser.getFirstName(), response.jsonPath().getString("firstName"));
        assertEquals(newUser.getLastName(), response.jsonPath().getString("lastName"));
        assertEquals(newUser.getEmail(), response.jsonPath().getString("email"));
        assertNotNull(response.jsonPath().getString("id"));
    }
}
