package cn.herbert.reminder_java.controller;

import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.User;
import cn.herbert.reminder_java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


//    @GetMapping("/user/findByEmail")
//    @ResponseBody
//    public String getUser(@RequestBody String email) {
//        return userService.findByEmail(email);
//    }

    @GetMapping("/user")
    public List<User> getUsers() {
        List<User> users = userService.getUsers();
        for (User user : users) {
            System.out.println(user);
        }

        return users;
    }

}
