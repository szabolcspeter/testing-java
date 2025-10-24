package com.appsdeveloperblog.tutorials.junit.io;

import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class UserEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testUserEntity_whenValidUserDetailsProvided_shouldReturnStoredUserDetails() {

        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Szabi");
        userEntity.setLastName("Peter");
        userEntity.setEmail("email@test.com");
        userEntity.setEncryptedPassword("12345678");

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
}
