package cn.herbert.reminder_java.pojo;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Data
@TableName("food")
public class Food {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    private String category;
    private String name;
    @TableField("image_url")
    private String imageUrl;
    @TableField("production_date")
    private LocalDateTime productionDate;
    @TableField("shelf_life_days")
    private Integer shelfLifeDate;
    @TableField("expiry_date")
    private LocalDateTime expiryDate;
    private Integer quantity;
    private String status;
    @TableField("created_at")
    private LocalDateTime createdAt;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    private String unit;
    private String info;
}
