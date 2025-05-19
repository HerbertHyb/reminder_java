package cn.herbert.reminder_java.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoodDto {
    private Integer id;
    private String category;
    private String name;
    private String imageUrl;
    private String productionDate;
    private Integer shelfLifeDays;
    private Integer quantity;
    private String unit;
    private String info;
}
