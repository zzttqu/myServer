package com.myserver.service;

import com.myserver.Pojo.EmailCode;

public interface SendMailService {
    String sendCodeToEmail(String email,String method);
    Boolean validateCode(EmailCode emailCode);
}
