package com.appsdeveloperblog.UsersServiceRESTAssured.ui;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class UsersControllerWithTestContainerITest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0");

    @Order(1)
    @Test
    void testContainerIsRunning() {
        assertTrue(mysqlContainer.isRunning());
    }

    @Order(2)
    @Test
    void testCreateUser_whenValidDetailsProvided_returnsCreatedUser() {

        // Arrange

        // Act
        // *** It uses Fluent API ***
        given() // setup HTTP details
                .contentType(ContentType.JSON) // longer syntax  .header("Content-Type", "application/json")
                .accept(ContentType.JSON) // longer syntax .header("Accept", "application/json")
        .when() // used to specify HTTP method and API endpoint that we want to call
        .then(); // we verify HTTP response
        // Assert
    }
}
