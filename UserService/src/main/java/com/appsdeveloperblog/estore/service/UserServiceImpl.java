package com.appsdeveloperblog.estore.service;

import com.appsdeveloperblog.estore.data.UsersRepository;
import com.appsdeveloperblog.estore.model.User;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User createUser(
            String firstName,
            String lastName,
            String email,
            String password,
            String repeatPassword
    ) {
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("User's first name is empty");
        }

        User user = new User(firstName, lastName, email, UUID.randomUUID().toString());
        boolean isUserCreated;

        try {
            isUserCreated = usersRepository.save(user);
        } catch (RuntimeException ex) {
            throw new UserServiceException(ex.getMessage());
        }

        if (!isUserCreated) {
            throw new UserServiceException("Could not create user");
        }
        return user;
    }
}
