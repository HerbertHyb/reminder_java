package cn.herbert.reminder_java.config;

import cn.herbert.reminder_java.mapper.FoodMapper;
import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.Food;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleTask {
    @Autowired
    FoodMapper foodMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(cron = "0 0 9 * * ?") // 每天上午9点执行
    public void sendEmail() {
        // 取出所有用户
        userMapper.selectList(null).forEach(user -> {
            // 获取用户邮箱
            String userEmail = user.getEmail();
            // 邮件标题
            String subject = "Food Expiry Reminder";
            // 邮件发送者
            String from = "reminder2025@126.com";

            List<String> foodList = new ArrayList<>();

            // 遍历该用户所有的食品
            foodMapper.selectList(new QueryWrapper<Food>().eq("user_id", user.getId())).forEach(food -> {
                // 检查剩余天数
                long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), food.getExpiryDate());
                remainingDays += 1;

                // 如果剩余天数小于等于3天，则添加到邮件内容
                if (remainingDays > 3) {
                    return;
                }

                if (remainingDays > 0) {
                    foodList.add(food.getName() + " will expire in " + remainingDays + " days");
                } else if (remainingDays == 0) {
                    foodList.add(food.getName() + " is expired");
                }
            });
            String content = "Food Expiry Reminder\n\n" + String.join("\n", foodList);

            if (!foodList.isEmpty()){
                // 发邮件
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject(subject);
                mailMessage.setText(content);
                mailMessage.setTo(userEmail);
                mailMessage.setFrom(from);
                javaMailSender.send(mailMessage);
            }
        });
    }

    @Scheduled(cron = "0 0 23 * * ?") // 每天晚上11点执行
    public void updateStatus() {
        // 取出所有数据
        foodMapper.selectList(null).forEach(food -> {
            // 检查剩余天数
            long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), food.getExpiryDate());
            remainingDays += 1;

            if (remainingDays == 0) {
                // 更新状态为过期
                food.setStatus("expired");
                foodMapper.updateById(food);
            } else if (remainingDays > 0 && remainingDays <= 3) {
                // 更新状态为即将过期
                food.setStatus("approximate");
                foodMapper.updateById(food);
            }
        });
    }
}
