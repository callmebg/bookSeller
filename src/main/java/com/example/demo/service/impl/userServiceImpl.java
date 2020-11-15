package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.dto.user;
import com.example.demo.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userServiceImpl implements userService {
    @Autowired
    UserMapper userMapper;
    @Override
    public void addUser(user u) {
        userMapper.insert(u);
    }

    @Override
    public String getPassword(String uid) {
        return userMapper.getUserPasswordById(uid);
    }

    @Override
    public List<String> getAllUsername(String username) {
        return userMapper.getAllUsername(username);
    }

    @Override
    public void changePassword(String newPassword,String uid) {
        userMapper.changePassword(newPassword,uid);
    }

    @Override
    public void changePermissions(String permission,String uid) {
        userMapper.changePermissions(permission,uid);
    }

    @Override
    public List<String> getAllEmail(String email) {
        return userMapper.getAllEmail(email);
    }


}
