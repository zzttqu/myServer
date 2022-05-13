package com.myserver.service;

import com.myserver.Dto.UserInfoDto;

public interface UserService {
    UserInfoDto checkPassword(String userKey);

    Boolean updateUserLoginInfo(Integer uid, String ip);

    String  sendCode(String email);

    Boolean changePassword(Integer uid, String userKey);

    Boolean changeUsername(Integer uid, String username);

    Boolean getUserLogin(Integer uid);
}
