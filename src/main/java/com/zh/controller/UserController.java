package com.zh.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zh.config.TypeException;
import com.zh.model.UserModel;
import com.zh.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@Controller
@Setter(onMethod_ = {@Autowired})
// @RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private UserService userService;

    @PostMapping("/api/register")
    public ResponseEntity<JSONObject> registerUser(@RequestBody Map<String, Object> body) {
//        int id = (Integer) body.get("id");
        String username = (String) body.get("name");
        String password = (String) body.get("password");

        try {
            UserModel userRegistered = userService.registerUser(username, password);

            // 注册成功
            JSONObject response = new JSONObject();
            response.put("code", "200");
            response.put("mesg", "注册成功");
            JSONObject data = (JSONObject) JSON.toJSON(userRegistered);
            response.put("data", data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            // 数据库违反约束，例如主键冲突
            JSONObject response = new JSONObject();
            response.put("code", "422");
            response.put("mesg", "注册失败");
            response.put("data", new JSONObject());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
//        return userService.registerUser(username, password);
    }

    @PostMapping("/api/login")
//    @RequestBody
    public JSONObject loginUser(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("name");
        String password = (String) body.get("password");
        UserModel userFound = userService.loginUser(username, password);

        JSONObject response = new JSONObject();
        if (userFound != null) {
            // 登录成功
            response.put("code", "200");
            response.put("mesg", "登录成功");
            JSONObject data = new JSONObject();
            data.put("token", "token114514");
            response.put("data", data);
        } else {
            // 登录失败
            response.put("code", "401");
            response.put("mesg", "账号或密码错误");
            response.put("data", new JSONObject());
        }

        return response;
    }
}
