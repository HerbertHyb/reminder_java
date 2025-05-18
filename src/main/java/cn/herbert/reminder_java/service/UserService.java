package cn.herbert.reminder_java.service;

import cn.herbert.reminder_java.auth.Msg;
import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.User;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public String getUserById(String id) {
        User dbUser = userMapper.selectById(id);
        Msg msg = null;
        if (dbUser == null) {
            msg = Msg.fail("User not found");
            return JSONUtil.toJsonStr(msg);
        } else {
            msg = Msg.success("User found").add("user", dbUser);
        }
        return JSONUtil.toJsonStr(msg);

    }
}
