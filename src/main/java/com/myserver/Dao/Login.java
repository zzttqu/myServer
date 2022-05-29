package com.myserver.Dao;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 类型：Dao
 * 作用：数据库中登录信息
 *
 * @author 张天奕
 */
@Data
@TableName(value = "login")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Login {
    /**
     * 登录信息id
     */
    @TableId(type = IdType.AUTO)
    Integer id;
    /**
     * 登录用户的uid
     */
    Integer uid;
    /**
     * 登录用户的登录ip地址
     */
    String lastip;
    /**
     * 登录时间
     */
    @TableField(fill = FieldFill.INSERT)
    LocalDateTime dateTime;

    /**
     * 创建登录信息
     *
     * @param uid 登录的用户uid
     * @param lastip 用户登录的ip
     */
    public Login(Integer uid, String lastip) {
        this.uid = uid;
        this.lastip = lastip;
    }
}
