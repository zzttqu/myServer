package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类型：Dao
 * 作用：数据库中用户全部信息
 *
 * @author 张天奕
 */
@Data
@TableName(value = "user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MyUser {
    /**
     * 用户的uid
     */
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 用户名
     * 限制10个字符以内
     */
    private String username;
    /**
     * 密码，最好用md5
     */
    private String userKey;

    private String rawPassword;
    /**
     * 用户状态<br>
     * 正常 0<br>
     * 封禁 1<br>
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;
    /**
     * 上次登录日期<br>
     * 已整合到Login表中
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime lastLoginTime;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 登录次数<br>
     * 已整合到Login表中
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer loginTimes;
    /**
     * 累计经验
     */
    private Integer exp;
    /**
     * 头像id
     */
    private Integer avatar;

}
