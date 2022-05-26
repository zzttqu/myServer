package com.myserver.controller;

import com.myserver.Dao.ExpCount;
import com.myserver.Dto.UserInfoChangeDto;
import com.myserver.service.ExpInfoService;
import com.myserver.service.RegisterService;
import com.myserver.service.SignInService;
import com.myserver.service.UserService;
import com.myserver.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 类型：Controller
 * 作用：用户相关操作（签到、经验信息）的api
 *
 * @author 张天奕
 * @see com.myserver.service.Impl.SignInServiceImpl 服务层实现类
 * @see com.myserver.service.Impl.ExpInfoServiceImpl 服务层实现类
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Resource
    private SignInService signInService;
    @Resource
    private ExpInfoService expInfoService;
    @Resource
    private RegisterService registerService;
    @Resource
    private UserService userService;
    @Value("${file.uploadFolder}")
    private String uploadFolder;

    /**
     * 签到
     *
     * @param request 从session中获取uid
     * @return 签到获得的经验值
     */
    //签到
    @GetMapping("/signin")
    public R signIn(HttpServletRequest request) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        Integer exp = signInService.signIn(uid);
        if (exp != 0) {
            return new R(1, 1);
        }
        return new R(1, 0);
    }

    /**
     * 获取签到过的数据
     *
     * @param request 从session中获取uid
     * @return SignInDto的list
     */
    //获取签到过的数据
    @GetMapping("/signinlist")
    public R sigInList(HttpServletRequest request) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        return new R(1, signInService.signInList(uid));
    }

    /**
     * 获取该用户全部exp数据
     *
     * @param request 从session中获取uid
     * @return ExpCountDto的list
     */
    @GetMapping("/explist")
    public R expList(HttpServletRequest request) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        return new R(1, expInfoService.getAllExpNum(uid));
    }

    /**
     * 修改用户头像和用户名
     *
     * @param userInfo 用户提交的修改信息
     * @param request  获取uid
     * @return 1 1为修改成功
     */
    @PostMapping("/updateinfo")
    public R uploadAvatar(@RequestBody UserInfoChangeDto userInfo, HttpServletRequest request) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        if (userInfo.getAvatar() != null && !userInfo.getAvatar().equals("")) {
            return new R(1, userService.changeAvatar(uid, userInfo.getAvatar()) ? 1 : 0);
        }
        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
            if (registerService.checkUserName(userInfo.getUsername()) == 0) {
                return new R(1, userService.changeUsername(uid, userInfo.getUsername()) ? 1 : 0);
            }
            //有重复的了直接返回报错
            return new R(1, 0);
        }
        return new R(0, 0);
    }

    /**
     * 获取当前用户总经验值
     *
     * @param request 从session中获取uid
     * @return int数据
     */
    //获取当前用户经验值
    @GetMapping("/exp")
    public R expInfo(HttpServletRequest request) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        Integer exp = expInfoService.getUserTotalExp(uid);
        List<ExpCount> allExpNum = expInfoService.getAllExpNum(uid);
        if ((exp == null) || (exp == -1)) {
            return new R(1, new R(0, allExpNum));
        } else {
            return new R(1, new R(exp, allExpNum));
        }
    }
}
