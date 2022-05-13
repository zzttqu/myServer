package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类型：Dao
 * 作用：数据库中的dialog对象
 *
 * @author 张天奕
 */
@Data
@TableName(value = "dialog")
public class Dialog {
    /**
     * dialog的id
     */
    @TableId(value = "dialog_id", type = IdType.AUTO)
    private Integer dialog_id;
    /**
     * 创建该dialog的用户的uid
     */
    private Integer uid;
    /**
     * 创建该dialog的用户的用户名
     */
    private String username;
    /**
     * 该dialog的文字内容
     */
    private String text;
    /**
     * 该dialog的图片内容
     * 内容为图片地址的json数组
     */
    private String img;
    /**
     * 该dialog的当前状态
     * 0为标准
     */
    private Integer status;
    /**
     * 该dialog的创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
    /**
     * 该dialog的点赞数量
     */
    private Integer likes;
}
