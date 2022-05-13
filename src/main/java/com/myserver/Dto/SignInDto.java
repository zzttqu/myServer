package com.myserver.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 张天奕
 * 类型：DTO
 * 作用：发送给前端的签到信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto implements Serializable {

    /**
     * 表示签到的天数
     */
    private Integer day;

    /**
     * 1表示已经签到, 0表示未签到
     */
    private Integer flag;
}

// 如果还有其他业务字段就加在后面, 如积分, 如金币

