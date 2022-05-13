package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//

/**
 * @author 张天奕
 * 类型：DTO
 * 作用：接收前端传入的用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    /**
     * 用户输入的含uid和password的信息
     * 可以改为md5值对比
     */
    private String info;

}
