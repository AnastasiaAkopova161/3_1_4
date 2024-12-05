package com.springboot3_1_4.service;


import com.springboot3_1_4.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    // Get Users - получение списка пользователей
    ResponseEntity<String> getUsers();

    ResponseEntity<String> saveUser(User user);
    ResponseEntity<String> updateUser(Long userId, User updatedUser);

    ResponseEntity<String> deleteUser(Long userId);
    String getSessionId();
}

