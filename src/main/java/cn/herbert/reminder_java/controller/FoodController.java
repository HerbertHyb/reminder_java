package cn.herbert.reminder_java.controller;

import cn.herbert.reminder_java.pojo.Food;
import cn.herbert.reminder_java.service.FoodService;
import cn.herbert.reminder_java.utils.JwtToken;
import cn.herbert.reminder_java.utils.JwtUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class FoodController {
    @Autowired
    private FoodService foodService;

    @GetMapping("/food/info")
    @JwtToken
    public String getAllFood(@RequestHeader("Authorization") String token) {
        // 1. 获取用户ID
        String userId = JwtUtil.getUserId(token);
        return foodService.getAllFoodByUserId(userId);
    }

    @PostMapping("/food/add")
    @JwtToken
    public String addFood(@RequestBody Food food, @RequestHeader("Authorization") String token) {
        food.setUserId(Integer.valueOf(JwtUtil.getUserId(token)));
        food.setExpiryDate(LocalDateTime.now());
        food.setCreatedAt(LocalDateTime.now());
        food.setStatus("0");
        food.setProductionDate(LocalDateTime.now());
        food.setShelfLifeDate(10);
        food.setQuantity(1);
        food.setUnit("kg");
        food.setCategory("0");
        food.setImageUrl("https://example.com/image.jpg");
        food.setInfo("This is a test food item.");
        food.setName("Test Food");
        food.setUpdatedAt(LocalDateTime.now());

        return foodService.addFood(food);
    }

    @PostMapping("/food/update")
    @JwtToken
    public String updateFood(@RequestBody Food food, @RequestHeader("Authorization") String token) {
        food.setUserId(Integer.valueOf(JwtUtil.getUserId(token)));
        food.setUpdatedAt(LocalDateTime.now());
        return foodService.updateFood(food);
    }

    @PostMapping("/food/delete")
    @JwtToken
    public String deleteFood(@RequestBody Food food ) {
        return foodService.deleteFoodById(food.getId().toString());
    }


}
