package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

@DataJpaTest
public class UsersRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    private final String userId1 = UUID.randomUUID().toString();
    private final String userId2 = UUID.randomUUID().toString();
    private final String email1 = "test@test.com";
    private final String email2 = "test2@test.com";

    @BeforeEach
    void setup() {
        UserEntity user1 = new UserEntity();
        user1.setUserId(userId1);
        user1.setFirstName("Szabi");
        user1.setLastName("Peter");
        user1.setEmail(email1);
        user1.setEncryptedPassword("12345678");
        testEntityManager.persistAndFlush(user1);

        UserEntity user2 = new UserEntity();
        user2.setUserId(userId2);
        user2.setFirstName("John");
        user2.setLastName("Sears");
        user2.setEmail(email2);
        user2.setEncryptedPassword("abcdefg1");
        testEntityManager.persistAndFlush(user2);
    }

    @Test
    void testFindByEmail_whenGivenCorrectEmail_returnsUserEntity() {

        // Act
        UserEntity dbUser = usersRepository.findByEmail(email1);

        // Assert
        Assertions.assertEquals(email1, dbUser.getEmail(),
                "Returned email address does not match the expected value");
    }

    @Test
    void testFindByUserId_whenGivenCorrectUserId_returnsUserEntity() {

        // Act
        UserEntity dbUser = usersRepository.findByUserId(userId2);

        // Assert
        Assertions.assertNotNull(dbUser,
                "UserEntity object should not be null");

        Assertions.assertEquals(userId2, dbUser.getUserId(),
                "Returned userId does not match the expected value");
    }

    @Test
    void testFindUsersWithEmailEndingWith_whenGivenEmailDomain_returnsUsersWithGivenDomain() {

        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Janos");
        userEntity.setLastName("Kovacs");
        userEntity.setEmail("test@gmail.com");
        userEntity.setEncryptedPassword("123456789");
        testEntityManager.persistAndFlush(userEntity);

        String emailDomainName = "@gmail.com";

        // Act
        List<UserEntity> users = usersRepository.findUsersWithEmailEndingWith(emailDomainName);

        // Assert
        Assertions.assertEquals(1, users.size(),
                "There should be only one user in the list");

        Assertions.assertTrue(users.get(0).getEmail().endsWith(emailDomainName));
    }
}
