package com.myserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myserver.Dto.LoginDto;
import com.myserver.Dto.UserInfoCheckDto;
import com.myserver.Dto.UserInfoDto;
import com.myserver.Pojo.EmailCode;
import com.myserver.config.myannotation.AccessLimit;
import com.myserver.utils.R;
import com.myserver.Dao.MyUser;
import com.myserver.service.RegisterService;
import com.myserver.service.SendMailService;
import com.myserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 类型：Controller
 * 作用：登录和注册相关操作的api
 *
 * @author 张天奕
 * @see com.myserver.service.Impl.UserServiceImpl 服务层实现类
 * @see com.myserver.service.Impl.RegisterServiceImpl 服务层实现类
 * @see com.myserver.service.Impl.SendMailServiceImpl 服务层实现类
 */
@Slf4j
@RestController
@RequestMapping("/landr")
public class LoginRegController {
    @Autowired
    private UserService userService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private SendMailService sendMailService;

    /**
     * 基本功能检查username和email是否重复
     *
     * @param userInfo 用户名和用户邮箱地址
     * @return 0，success表示未注册<br>
     * 1, "NameRepeated"<br>
     * 1, "emailRepeated"<br>
     * 三种情况
     */
    @AccessLimit(maxCount = 10, seconds = 30)
    @PostMapping()
    public R userInfoCheck(@RequestBody UserInfoCheckDto userInfo) {
        String username = userInfo.getUsername();
        String email = userInfo.getEmail();
        UserInfoCheckDto userInfoCheckDto = new UserInfoCheckDto();
        if (username != null && !username.equals("")) {
            Integer uidName = registerService.checkUserName(username);
            if (uidName != 0) {
                userInfoCheckDto.setUid(uidName);
            }
            else {
                userInfoCheckDto.setUsername("no");
            }
        }
        if (email != null && !email.equals("")) {
            Integer uidEmail = registerService.checkUserEmail(email);
            if (uidEmail != 0) {
                userInfoCheckDto.setUid(uidEmail);
            }
            else {
                userInfoCheckDto.setEmail("no");
            }
        }
        return new R(1, userInfoCheckDto);
    }

    /**
     * 登录1是邮箱，2是用户名
     *
     * @param loginDto 含有uid和用户密码的信息
     * @param request  创建session
     * @return 登录是否成功 boolean
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto, HttpServletRequest request){
        String userKey = loginDto.getInfo();
        String ip = request.getHeader("x-real_ip");
        UserInfoDto sqlUser = userService.checkPassword(userKey);
        //如果密码验证通过
        // log.info(" ip" + ip + " attempt to login");
        if (sqlUser.getStatus() == -1) {
            return new R(1, -1);
        }
        if (sqlUser.getUsername() != null) {
            HttpSession session = request.getSession();
            session.setAttribute("username", sqlUser.getUsername());
            session.setAttribute("uid", sqlUser.getUid());
            //不适用nginx的时候跨域request，使用的话就得去header里边找了
            //如果更新用户登录次数等操作失败也算登录失败
            if (userService.updateUserLoginInfo(sqlUser.getUid(), ip)) {
                return new R(1, sqlUser);
            }
            else {
                log.info(" ip" + ip + " login failed because of server. Username: " + sqlUser.getUsername());
                return new R(0, 0);
            }
        }
        else {
            log.info(" ip" + ip + " login failed because of wrong password. Username: " + sqlUser.getUsername());
            return new R(1, 0);
        }
    }

    /**
     * 检查session是否还存活
     *
     * @param request 找session咯
     * @return 存活就返回uid
     */
    @GetMapping("/check")
    public R check(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            return new R(1, 1);
        }
        else {
            return new R(1, 0);
        }
    }

    /**
     * 登出 session失活
     *
     * @param request 获取session
     * @return "Logout success"
     */
    @GetMapping("/logout")
    public R logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return new R(1, 1);
    }

    /**
     * 注册相关信息
     *
     * @param myUser  这个其实只用了username password和email属性
     * @param request 这主要是放session里边东西
     * @return {@link UserInfoDto}
     */
    @PostMapping("/register")
    public R register(@RequestBody MyUser myUser, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //首先验证用户名
        if (!myUser.getUsername().equals("")) {
            if ((registerService.checkUserName(myUser.getUsername()) == 0) &&
                    (registerService.checkUserEmail(myUser.getEmail()) == 0)) {
                //验证邮箱通过后才能注册成功
                if (session.getAttribute("emailValid") == "true") {
                    if (registerService.createUser(myUser)) {
                        //保存用户头像移动到user中了
                        //邮箱验证在修改密码还要用，所以必须删除这个键值对
                        session.removeAttribute("emailValid");
                        session.setAttribute("username", myUser.getUsername());
                        session.setAttribute("uid", myUser.getUid());
                        UserInfoDto userInfo = new UserInfoDto(myUser.getUsername(), myUser.getUid(), 0);
                        return new R(1, userInfo);
                    }
                    else {
                        return new R(0, 1);
                    }
                }
                else {
                    return new R(1, 0);
                }
            }
            else {
                return new R(1, -1);
            }
        }
        else {
            return new R(0, 1);
        }
    }

    /**
     * 忘记密码修改密码
     *
     * @param request session
     * @param myUser  {@link MyUser}
     * @return Boolean 0
     */
    //忘记密码修改密码
    //需要发送修改密码的邮箱和修改后的密码
    @PostMapping("/password")
    public R passwordChange(HttpServletRequest request, @RequestBody MyUser myUser) {
        //传过来的是email
        HttpSession session = request.getSession();

        if (session.getAttribute("emailValid") != null) {
            if (userService.changePassword(myUser.getUid(), myUser.getUserKey())) {
                return new R(1, 1);
            }
            else {
                return new R(1, 0);
            }
        }
        else {
            return new R(0, 0);
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param email  邮箱地址
     * @param method 修改密码还是注册
     * @return Boolean,"Wrong method"
     */
    //发送邮箱验证码
    @GetMapping("/code")
    public R codeSend(String email, Integer method) {
        if (email != null && method != null) {
            if (method == 1) {
                if (registerService.sendCode(email).equals("success")) {
                    return new R(1, 1);
                }
                else {
                    return new R(1, 0);
                }
            }
            else if (method == 2) {
                if (userService.sendCode(email).equals("success")) {
                    return new R(1, 2);
                }
                else {
                    return new R(1, 0);
                }
            }
            else {
                new R(1, "Wrong method");
            }
        }
        return new R(1, "Wrong method");
    }

    //验证邮箱

    /**
     * 邮箱验证码验证
     *
     * @param request   session
     * @param emailCode 邮箱和验证码的组合
     * @return 验证是否成功 boolean
     */
    @PostMapping("/code")
    public R codeReceive(HttpServletRequest request, @RequestBody EmailCode emailCode) {
        HttpSession session = request.getSession();
        if (sendMailService.validateCode(emailCode)) {
            session.setAttribute("emailValid", "true");
            return new R(1, 1);
        }
        else {
            return new R(0, 0);
        }
    }

    //获取是否需要看新的
    @GetMapping("/watchnew")
    public R watchNew(HttpServletRequest request) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        return new R(1, userService.getUserLogin(uid));
    }

}
