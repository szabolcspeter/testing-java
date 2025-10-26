package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class UsersRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    @Test
    void testFindByEmail_whenGivenCorrectEmail_returnsUserEntity() {

        // Arrange
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID().toString());
        user.setFirstName("Szabi");
        user.setLastName("Peter");
        user.setEmail("email@test.com");
        user.setEncryptedPassword("12345678");

        testEntityManager.persistAndFlush(user);

        // Act
        UserEntity dbUser = usersRepository.findByEmail(user.getEmail());

        // Assert
        Assertions.assertEquals(user.getEmail(), dbUser.getEmail(),
                "Returned email address does not match the expected value");
    }
}
