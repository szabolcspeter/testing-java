package com.appsdeveloperblog.tutorials.junit.io;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class UserEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Szabi");
        userEntity.setLastName("Peter");
        userEntity.setEmail("email@test.com");
        userEntity.setEncryptedPassword("12345678");
    }

    @Test
    void testUserEntity_whenValidUserDetailsProvided_shouldReturnStoredUserDetails() {

        // Act
        UserEntity dbUserEntity = testEntityManager.persistAndFlush(userEntity);

        // Assert
        Assertions.assertTrue(dbUserEntity.getId() > 0);
        Assertions.assertEquals(userEntity.getUserId(), dbUserEntity.getUserId());
        Assertions.assertEquals(userEntity.getFirstName(), dbUserEntity.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), dbUserEntity.getLastName());
        Assertions.assertEquals(userEntity.getEmail(), dbUserEntity.getEmail());
        Assertions.assertEquals(userEntity.getEncryptedPassword(), dbUserEntity.getEncryptedPassword());
    }

    @Test
    void testUserEntity_whenFirstNameIsTooLong_shouldThrowException() {

        // Arrange
        userEntity.setFirstName("123456789012345678901234567890123456789012345678901234567890");

        // Act & Assert
        Assertions.assertThrows(PersistenceException.class, () -> {
            testEntityManager.persistAndFlush(userEntity);
        }, "Was expecting a PersistenceException to be thrown");
    }

    @Test
    void testUserEntity_whenExistingUserIdProvided_shouldThrowException() {

        // Arrange
        userEntity.setUserId("1");
        testEntityManager.persistAndFlush(userEntity);

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setFirstName("test");
        newUserEntity.setLastName("test");
        newUserEntity.setEmail("test@test.com");
        newUserEntity.setEncryptedPassword("test");

        newUserEntity.setUserId("1");

        // Act & Assert
        Assertions.assertThrows(PersistenceException.class, () -> {
            testEntityManager.persistAndFlush(newUserEntity);
        }, "Was expecting a PersistenceException to be thrown");
    }
}
