package com.zh.service;

import com.zh.model.UserModel;

public interface UserService {
    UserModel registerUser(String username, String password);

    UserModel loginUser(String username, String password);
}
