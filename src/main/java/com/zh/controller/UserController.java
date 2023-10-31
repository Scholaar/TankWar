package com.zh.controller;

import com.zh.config.TypeException;
import com.zh.model.UserModel;
import com.zh.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@Controller
@Setter(onMethod_ = {@Autowired})
// @RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public UserModel registerUser(@RequestBody Map<String, Object> body) {
        int id = (Integer) body.get("id");
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        return userService.registerUser(username, password);
    }

    @PostMapping("/login")
    public UserModel loginUser(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        UserModel userFound = userService.loginUser(username, password);
        if (userFound==null) {
            throw new TypeException("用户不存在");
        }

        return userFound;
    }
}
