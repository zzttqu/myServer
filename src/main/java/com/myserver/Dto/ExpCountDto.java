package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类型：DTO
 * 作用：返回的用户的经验信息
 *
 * @author 张天奕
 */
@Data
@AllArgsConstructor
public class ExpCountDto {
    /**
     * 该用户该经验获得项
     */
    private Integer cause;
    /**
     * 该用户该经验项获得次数
     */
    private Integer count;
}
