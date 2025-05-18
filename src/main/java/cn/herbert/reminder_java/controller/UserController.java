package cn.herbert.reminder_java.controller;

import cn.herbert.reminder_java.mapper.FoodMapper;
import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.Food;
import cn.herbert.reminder_java.pojo.User;
import cn.herbert.reminder_java.service.LoginService;
import cn.herbert.reminder_java.service.UserService;
import cn.herbert.reminder_java.utils.JwtToken;
import cn.herbert.reminder_java.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @PostMapping("/auth/loginByPassword")
    public String loginByPassword(@RequestBody User user) {
        return loginService.loginByPassword(user);
    }

    @PostMapping("/auth/info")
    @JwtToken
    public String getUserInfo(@RequestHeader("Authorization") String token) {
        // 1. 获取用户ID
        String userId = JwtUtil.getUserId(token);

        return userService.getUserById(userId);
    }
}
