package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类型：Dao
 * 作用：数据库中的post对象
 *
 * @author 张天奕
 */
@Data
@TableName(value = "posts")
public class Post {
    /**
     * post的id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 创建该post的用户的uid
     */
    private Integer uid;
    /**
     * 创建该post的用户的用户名
     */
    private String username;
    /**
     * 该post的文字内容
     */
    private String text;
    /**
     * 该post的图片内容
     * 内容为图片地址的json数组
     */
    private String img;
    /**
     * 该post的当前状态
     * 0为标准
     */
    private Integer status;
    /**
     * 该post的创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
    /**
     * 该post的点赞数量
     */
    private Integer likes;
    /**
     * 类别
     * 1为文字
     * 2为含图片
     */
    private Integer category;
}
