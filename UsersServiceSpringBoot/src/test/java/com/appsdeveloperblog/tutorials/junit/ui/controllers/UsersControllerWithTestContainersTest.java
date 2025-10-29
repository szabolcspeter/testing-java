package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.security.SecurityConstants;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsersControllerWithTestContainersTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String authorizationToken;

//    @Container
    @ServiceConnection
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.0");
//            .withDatabaseName("photo_app")
//            .withUsername("szabi")
//            .withPassword("szabi");

//    @DynamicPropertySource
//    private static void overrideProperties(DynamicPropertyRegistry registry) {
//
//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//    }

    static {
        mySQLContainer.start();
    }

    @Test
    @DisplayName("The MySQL container is created and is running")
    @Order(1)
    void testContainerIsRunning() {

        Assertions.assertTrue(mySQLContainer.isCreated(),
                "MySQL container has not been created");

        Assertions.assertTrue(mySQLContainer.isRunning(),
                "MySQL container is not running");
    }

    @Test
    @DisplayName("User can be created")
    @Order(2)
    void testCreateUser_whenValidDetailsProvided_returnsUserDetails() throws JSONException {

        // Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Szabi");
        userDetailsRequestJson.put("lastName", "Peter");
        userDetailsRequestJson.put("email", "email@test.com");
        userDetailsRequestJson.put("password", "12345678");
        userDetailsRequestJson.put("repeatPassword", "12345678");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<UserRest> createdUserDetailsEntity =
                testRestTemplate.postForEntity("/users", request, UserRest.class);

        UserRest createdUserDetails = createdUserDetailsEntity.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, createdUserDetailsEntity.getStatusCode());
        Assertions.assertNotNull(createdUserDetails);
        Assertions.assertEquals(userDetailsRequestJson.getString("firstName"),
                createdUserDetails.getFirstName(),
                "Returned user's first name seems to be incorrect");
        Assertions.assertEquals(userDetailsRequestJson.getString("lastName"),
                createdUserDetails.getLastName(),
                "Returned user's last name seems to be incorrect");
        Assertions.assertEquals(userDetailsRequestJson.getString("email"),
                createdUserDetails.getEmail(),
                "Returned user's email seems to be incorrect");
        Assertions.assertFalse(createdUserDetails.getUserId().trim().isEmpty(),
                "User id should not be empty");
    }

    @Test
    @DisplayName("GET /users requires JWT")
    @Order(3)
    void testGetUsers_whenMissingJWT_returns403() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange("/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        // Assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),
                "HTTP Status code 403 Forbidden should have been returned");
    }

    @Test
    @DisplayName("/login works")
    @Order(4)
    void testUserLogin_whenValidCredentialsProvided_returnsJWTinAuthorizationHeader() throws JSONException {

        // Arrange
        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("email", "email@test.com");
        loginCredentials.put("password", "12345678");

        HttpEntity<String> request = new HttpEntity<>(loginCredentials.toString());

        // Act
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/users/login",
                request,
                null);

        authorizationToken = response.getHeaders()
                .getValuesAsList(SecurityConstants.HEADER_STRING)
                .get(0);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "HTTP Status code should be OK");

        Assertions.assertNotNull(authorizationToken,
                "Response should contain Authorization header with JWT");

        Assertions.assertNotNull(response.getHeaders()
                        .getValuesAsList("UserID")
                        .get(0),
                "Response should contain UserID in a response header");
    }

    @Test
    @Order(5)
    @DisplayName("GET /users works")
    void testGetUsers_whenValidJWTProvided_returnsUsers() {

        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange("/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "HTTP Status code should be 200");

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size(),
                "There should be exactly 1 user in the list");
    }
}
