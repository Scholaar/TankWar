package com.zh.service;

import lombok.Setter;
import com.zh.model.UserModel;
import com.zh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Setter(onMethod_ = {@Autowired})
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserModel registerUser(String username, String password) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Override
    public UserModel loginUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
