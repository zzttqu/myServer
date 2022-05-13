package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型：Dto
 * 作用：发送用户基本信息
 *
 * @author 张天奕
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户uid
     */
    private Integer uid;
    /**
     * 用户点赞列表
     */
    private List<Integer> likes = new ArrayList<>();
    /**
     * 封禁状态
     */
    private Integer status;


    public UserInfoDto(String username, Integer uid, Integer status) {
        this.username = username;
        this.uid = uid;
        this.status = status;
    }
}
