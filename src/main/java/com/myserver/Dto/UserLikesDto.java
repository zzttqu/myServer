package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 张天奕
 * 类型：DTO
 * 作用：发送给前端的用户点赞信息
 * @deprecated
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikesDto {
    /**
     * 该用户的uid
     */
    private Integer uid;
    private Integer dialog_id;
    private Integer status;
}
