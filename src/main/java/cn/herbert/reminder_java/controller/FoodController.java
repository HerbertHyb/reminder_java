package cn.herbert.reminder_java.controller;

import cn.herbert.reminder_java.pojo.Food;
import cn.herbert.reminder_java.pojo.FoodDto;
import cn.herbert.reminder_java.service.FoodService;
import cn.herbert.reminder_java.utils.JwtToken;
import cn.herbert.reminder_java.utils.JwtUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public String updateFood(@RequestBody Food food, @RequestHeader("Authorization") String token) {
        food.setUserId(Integer.valueOf(JwtUtil.getUserId(token)));
        food.setUpdatedAt(LocalDateTime.now());
        return foodService.updateFood(food);
    }

    @PostMapping("/food/delete")
    @JwtToken
    public String deleteFood(@RequestBody Food food) {
        return foodService.deleteFoodById(food.getId().toString());
    }


}
