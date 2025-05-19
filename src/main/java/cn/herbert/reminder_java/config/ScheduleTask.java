package cn.herbert.reminder_java.config;

import cn.herbert.reminder_java.mapper.FoodMapper;
import cn.herbert.reminder_java.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ScheduleTask {
    @Autowired
    FoodMapper foodMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(cron = "0 0 12 * * ?") // 每天中午12点执行
    public void sendEmail() {
        // 取出所有数据
        foodMapper.selectList(null).forEach(food -> {
            // 检查剩余天数
            long remainingDays = ChronoUnit.DAYS.between(food.getExpiryDate(), LocalDateTime.now());

            // 获取用户邮箱
            Integer userId = food.getUserId();
            String userEmail = userMapper.selectById(userId).getEmail();
            // 获取食物名称
            String foodName = food.getName();
            // 邮件内容
            String subject = "Food Expiry Reminder";
            String content = foodName + " is about to expire" + remainingDays + " days";
            // 邮件发送者
            String from = "reminder2025@126.com";

            if (remainingDays == 0) {
                // 发送过期邮件
                System.out.println("Food " + food.getName() + " is expired.");
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject(subject);
                mailMessage.setText(content);
                mailMessage.setTo(userEmail);
                mailMessage.setFrom(from);
                javaMailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            } else if (remainingDays > 0 && remainingDays <= 3) {
                // 发送即将过期邮件
                System.out.println("Food " + food.getName() + " is about to expire in " + remainingDays + " days.");
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject(subject);
                mailMessage.setText(content);
                mailMessage.setTo(userEmail);
                mailMessage.setFrom(from);
                javaMailSender.send(mailMessage);
                System.out.println("Email sent successfully");
            }
        });
    }

    @Scheduled(cron = "0 0 23 * * ?") // 每天晚上11点执行
    public void updateStatus() {
        // 取出所有数据
        foodMapper.selectList(null).forEach(food -> {
            // 检查剩余天数
            long remainingDays = ChronoUnit.DAYS.between(food.getExpiryDate(), LocalDateTime.now());

            if (remainingDays == 0) {
                // 更新状态为过期
                food.setStatus("expired");
                foodMapper.updateById(food);
            } else if (remainingDays <= 3) {
                // 更新状态为即将过期
                food.setStatus("approximate");
                foodMapper.updateById(food);
            }
        });
    }
}
