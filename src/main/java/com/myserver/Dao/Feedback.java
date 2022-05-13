package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 类型：Dao
 * 作用：数据库中反馈意见
 *
 * @author 张天奕
 */
@Data
@TableName(value = "feedback")
public class Feedback {
    /**
     * 万年不变的id
     */
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Integer feedback_id;
    /**
     * 创建反馈的uid
     * 如果登录没有就为空
     */
    private Integer uid;
    /**
     * 反馈的文字内容
     */
    private String text;
    /**
     * 提交反馈的时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime dateTime;
}
