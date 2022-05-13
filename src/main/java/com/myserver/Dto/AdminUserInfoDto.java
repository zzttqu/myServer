package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminUserInfoDto {
    private Integer uid;
    private String email;
    private String username;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private Integer status;
    private Integer exp;
}
