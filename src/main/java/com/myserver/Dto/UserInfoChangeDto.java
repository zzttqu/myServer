package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 张天奕
 * 类型：DTO
 * 作用：收到前端的用户修改个人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoChangeDto {
    /**
     * 用户修改自己的用户名
     */
    private String username;
    //private String password;
    /**
     * 修改自己的头像
     */
    private String avatar;
}
