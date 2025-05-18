package cn.herbert.reminder_java.service;

import cn.herbert.reminder_java.auth.Msg;
import cn.herbert.reminder_java.pojo.User;
import cn.herbert.reminder_java.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

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
                    .add("username", dbUser.getUsername())
                    .add("id", dbUser.getId())
                    .add("phone", dbUser.getPhone())
                    .add("email", dbUser.getEmail());
        }

        return JSONUtil.toJsonStr(msg);

    }
}
