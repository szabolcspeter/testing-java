package com.appsdeveloperblog.service;

import com.appsdeveloperblog.io.UsersDatabase;

import java.util.Map;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    UsersDatabase usersDatabase;

    public UserServiceImpl(UsersDatabase usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    @Override
    public String createUser(Map<String, String> userDetails) {
        String userId = UUID.randomUUID().toString();
        userDetails.put("userId", userId);
        usersDatabase.save(userId, userDetails);
        return userId;
    }

    @Override
    public Map<String, String> updateUser(String userId, Map<String, String> userDetails) {
        Map<String, String> existingUser = usersDatabase.find(userId);
        if(existingUser == null) throw new IllegalArgumentException("User not found");

        existingUser.put("firstName", userDetails.get("firstName"));
        existingUser.put("lastName", userDetails.get("lastName"));

        return usersDatabase.update(userId, existingUser);
    }

    @Override
    public Map<String, String> getUserDetails(String userId) {
        return usersDatabase.find(userId);
    }

    @Override
    public void deleteUser(String userId) {
        Map<String, String> existingUser = usersDatabase.find(userId);
        if(existingUser == null) throw new IllegalArgumentException("User not found");

        usersDatabase.delete(userId);
    }
}
