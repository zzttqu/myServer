package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 类型：Dao
 * 作用：数据库中用户登录信息
 *
 * @author 张天奕
 */
@Data
@TableName(value = "signin")
@AllArgsConstructor
@NoArgsConstructor
public class SignIn {
    /**
     * 登录信息id
     */
    @TableId(value = "id",type = IdType.AUTO)
    Integer id;
    /**
     * 登录的用户uid
     */
    Integer uid;
    /**
     * 登录时间
     */
    @TableField(fill = FieldFill.INSERT)
    LocalDateTime dateTime;
    /**
     * 连续登录时间
     */
    @TableField(fill = FieldFill.INSERT)
    Integer continueDays;
    /**
     * 该条登录的状态
     */
    @TableField(fill = FieldFill.INSERT)
    Integer status;

    public SignIn(Integer uid, LocalDateTime now) {
        this.uid=uid;
        this.dateTime=now;
    }
}
