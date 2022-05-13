package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
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
     *
     *
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
     *
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer loginTimes;
//    /**
//     * 登录ip<br>
//     * 已整合到Login表中
//     *
//     *
//     */
//    private String lastLoginIp;
    /**
     * 累计经验
     */
    private Integer exp;
    //这三个只是为了方便，不在数据库表中

    /**
     * 这个好像是用户的照片
     * 数据库中没有该字段
     */
    @TableField(exist = false)
    private String userInfo;
    //由于likes移动到了单独列表中了，为了不新建对象，就在这里加一项

}
