package com.appsdeveloperblog.service;

import java.util.Map;

public interface UserService {
    String createUser(Map<String, String> userDetails);
    Map<String, String> updateUser(String userId, Map<String, String> userDetails);
    Map<String, String> getUserDetails(String userId);
    void deleteUser(String userId);
}
