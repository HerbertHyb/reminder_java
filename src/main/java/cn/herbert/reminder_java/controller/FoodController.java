package cn.herbert.reminder_java.controller;

import cn.herbert.reminder_java.mapper.FoodMapper;
import cn.herbert.reminder_java.mapper.UserMapper;
import cn.herbert.reminder_java.pojo.Food;
import cn.herbert.reminder_java.pojo.FoodDto;
import cn.herbert.reminder_java.service.FoodService;
import cn.herbert.reminder_java.utils.JwtToken;
import cn.herbert.reminder_java.utils.JwtUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RestController
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/food/info")
    @JwtToken
    public String getAllFood(@RequestHeader("Authorization") String token) {
        // 1. 获取用户ID
        String userId = JwtUtil.getUserId(token);
        return foodService.getAllFoodByUserId(userId);
    }

    @PostMapping("/food/add")
    @JwtToken
    public String addFood(@RequestBody FoodDto foodDto, @RequestHeader("Authorization") String token) {
        System.out.println(foodDto);
        // 处理时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime productionDate = LocalDateTime.parse(foodDto.getProductionDate(), formatter);
        Food food = new Food(
                null,
                Integer.valueOf(JwtUtil.getUserId(token)),
                foodDto.getCategory(),
                foodDto.getName(),
                foodDto.getImageUrl(),
                productionDate,
                foodDto.getShelfLifeDays(),
                productionDate.plusDays(foodDto.getShelfLifeDays()),
                foodDto.getQuantity(),
                "fresh",
                LocalDateTime.now(),
                LocalDateTime.now(),
                foodDto.getUnit(),
                foodDto.getInfo(),
                false
        );
        System.out.println(food);

        return foodService.addFood(food);
    }

    @PostMapping("/food/update")
    @JwtToken
    public String updateFood(@RequestBody FoodDto foodDto, @RequestHeader("Authorization") String token) {
        System.out.println(foodDto);
        // 处理时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime productionDate = LocalDateTime.parse(foodDto.getProductionDate(), formatter);

        // 过期时间
        LocalDateTime expiryDate = productionDate.plusDays(foodDto.getShelfLifeDays());
        // 处理status
        String status = null;
        long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(),expiryDate);
        remainingDays+= 1;

        if (remainingDays <= 0) {
            status = "expired";
        } else if (remainingDays <= 3) {
            status = "approximate";
        } else {
            status = "fresh";
        }

        Food food = new Food(
                foodDto.getId(),
                Integer.valueOf(JwtUtil.getUserId(token)),
                foodDto.getCategory(),
                foodDto.getName(),
                foodDto.getImageUrl(),
                productionDate,
                foodDto.getShelfLifeDays(),
                expiryDate,
                foodDto.getQuantity(),
                status,
                null,
                LocalDateTime.now(),
                foodDto.getUnit(),
                foodDto.getInfo(),
                false
        );

        System.out.println(food);
        return foodService.updateFood(food);
    }

    @PostMapping("/food/delete")
    @JwtToken
    public String deleteFood(@RequestBody Food food) {
        return foodService.deleteFoodById(food.getId().toString());
    }

    @GetMapping("/food/remainingDays")
    @JwtToken
    public String getRemainingDays(@RequestParam("foodId") String foodId) {
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
        return "Email sent successfully";
    }


}
