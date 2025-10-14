package com.appsdeveloperblog.estore.service;

import com.appsdeveloperblog.estore.data.UsersRepository;
import com.appsdeveloperblog.estore.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UsersRepository usersRepository;
    String firstName;
    String lastName;
    String email;
    String password;
    String repeatPassword;

    @BeforeEach
    void init() {
        firstName = "Szabi";
        lastName = "Peter";
        email = "test@test.com";
        password = "12345678";
        repeatPassword = "12345678";
    }

    @DisplayName("User object created")
    @Test
    void testCreateUser_whenUserDetailsProvided_returnsUserObject() {

        // Arrange
        Mockito.when(usersRepository.save(any(User.class))).thenReturn(true);

        // Act
        User user =  userService.createUser(
                firstName,
                lastName,
                email,
                password,
                repeatPassword);

        // Assert
        assertNotNull(user, "The createUser() should not have returned null");
        assertEquals(firstName, user.getFirstName(), "User's first name is incorrect");
        assertEquals(lastName, user.getLastName(), "User's last name is incorrect");
        assertEquals(email, user.getEmail(), "User's email is incorrect");
        assertNotNull(user.getId(), "User id is missing");
        Mockito.verify(usersRepository, times(1))
                .save(any(User.class));
    }

    @DisplayName("Empty first name causes correct exception")
    @Test
    void testCreateUser_whenFirstNameIsEmpty_throwsIllegalArgumentException() {

        // Arrange
        String firstName = "";
        String expectedExceptionMessage = "User's first name is empty";

        // Act & Assert
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(
                    firstName,
                    lastName,
                    email,
                    password,
                    repeatPassword);
        }, "Empty first name should have thrown an Illegal Argument Exception");

        // Assert
        assertEquals(expectedExceptionMessage, thrown.getMessage(),
                "Exception error message is not correct");
    }
}
