package cn.herbert.reminder_java.service;

import cn.herbert.reminder_java.auth.Msg;
import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.User;
import cn.herbert.reminder_java.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserService userService;

    public String loginByPassword(User user) {
        // 1. 获取用户输入的用户名和密码
        String phone = user.getPhone();
        String password = user.getPassword();

        // 2. 查询数据库中是否存在该用户
        User dbUser = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        Msg msg = null;

        // 3. 如果用户不存在，返回错误信息
        if (dbUser == null) {
            msg = Msg.fail("Wrong account or password");
            return JSONUtil.toJsonStr(msg);

        }

        // 4. 如果用户存在，验证密码
        if (!dbUser.getPassword().equals(password)) {
            msg = Msg.fail("Wrong account or password");
        } else {
            String token = JwtUtil.getToken(dbUser.getId().toString());

            msg = Msg.success("Login successful").add("token", token)
                    .add("user", dbUser);
        }

        return JSONUtil.toJsonStr(msg);

    }

    public String register(User user) {
        // 1. 检查手机号是否已注册
        User existingUser = userService.getOne(new QueryWrapper<User>().eq("phone", user.getPhone()));
        // 2. 检查邮箱是否已注册
        User existingEmailUser = userService.getOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        Msg msg = null;

        if (existingUser != null) {
            msg = Msg.fail("Phone number already registered");
            return JSONUtil.toJsonStr(msg);
        }

        if (existingEmailUser != null) {
            msg = Msg.fail("Email already registered");
            return JSONUtil.toJsonStr(msg);
        }

        // 2. 如果手机号未注册，保存用户信息
        boolean save = userService.save(user);
        // 获取用户ID
        User dbUser = userService.getOne(new QueryWrapper<User>().eq("phone", user.getPhone()));

        if (save) {
            String token = JwtUtil.getToken(dbUser.getId().toString());
            msg = Msg.success("Registration successful").add("token", token)
                    .add("user", dbUser);
        } else {
            msg = Msg.fail("Registration failed");
        }

        return JSONUtil.toJsonStr(msg);
    }
}
