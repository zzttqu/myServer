package com.myserver.Pojo;

import lombok.Data;

@Data
public class RegisterUserInfo {
    private Integer uid;
    private String email;
    private String username;
    private String password;
    private String validateCode;
    private String file;
}
