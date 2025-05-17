package cn.herbert.reminder_java.service;

import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

//    public String findByEmail(String email) {
//        return getOne(new QueryWrapper<User>().eq("email", email)).toString();
//    }

    public List<User> getUsers() {
        return userMapper.selectList(null);
    }
}
