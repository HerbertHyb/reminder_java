package cn.herbert.reminder_java.service;

import cn.herbert.reminder_java.auth.Msg;
import cn.herbert.reminder_java.mapper.FoodMapper;
import cn.herbert.reminder_java.pojo.Food;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService extends ServiceImpl<FoodMapper, Food> {

    @Autowired
    private FoodMapper foodMapper;

    public String getAllFoodByUserId(String userId) {
        List<Food> foodList = foodMapper.selectList(new QueryWrapper<Food>().eq("user_id", userId));
        Msg msg = null;
        if (foodList == null || foodList.isEmpty()) {
            msg = Msg.fail("No food found");
            return msg.toString();
        } else {
            msg = Msg.success("Food found").add("food", foodList);
        }
        return msg.toString();
    }

    public String addFood(Food food) {
        boolean save = foodMapper.insert(food) > 0;
        Msg msg = null;
        if (save) {
            msg = Msg.success("Food added successfully");
        } else {
            msg = Msg.fail("Failed to add food");
        }
        return msg.toString();
    }

    public String deleteFoodById(String foodId) {
        boolean remove = foodMapper.deleteById(foodId) > 0;
        Msg msg = null;
        if (remove) {
            msg = Msg.success("Food deleted successfully");
        } else {
            msg = Msg.fail("Failed to delete food");
        }
        return msg.toString();
    }

    public String updateFood(Food food) {
        boolean update = foodMapper.updateById(food) > 0;
        Msg msg = null;
        if (update) {
            msg = Msg.success("Food updated successfully");
        } else {
            msg = Msg.fail("Failed to update food");
        }
        return msg.toString();
    }

}
