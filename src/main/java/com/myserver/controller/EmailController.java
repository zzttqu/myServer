package com.myserver.controller;

import com.myserver.Pojo.EmailCode;
import com.myserver.service.SendMailService;
import com.myserver.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 类型：Controller
 * 作用：邮件相关API
 * 正在开发ing
 *
 * @author 张天奕
 */
@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private SendMailService sendMailService;

    //验证邮箱
    @PostMapping("/code")
    public R codeReceive(HttpServletRequest request, @RequestBody EmailCode emailCode) {
        HttpSession session = request.getSession();
        if (sendMailService.validateCode(emailCode)) {
            session.setAttribute("emailValid", "true");
            return new R(1, true);
        }
        else {
            return new R(0, false);
        }
    }
}
