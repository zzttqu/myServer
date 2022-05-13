package com.myserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoCheckDto {
    private Integer uid;
    private String username;
    private String email;
}
