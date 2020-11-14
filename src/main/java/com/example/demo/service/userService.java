package com.example.demo.service;

import com.example.demo.dto.user;

import java.util.List;

public interface userService {

    void addUser(user u);

    String getPassword(String uid);

    List<String> getAllUsername(String username);

    void changePassword(String newPassword,String uid);

    void changePermissions(String permission,String id);
}
