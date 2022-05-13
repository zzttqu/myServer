package com.myserver.Intercepter;

import com.myserver.Dao.MyUser;
import com.myserver.Mapper.MyUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Resource
    MyUserMapper myUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        response.setHeader("cache-control","no-cache");
        if (uid == null) {
            response.sendError(401, "no Permission");
            return false;
        }
        MyUser myUser = myUserMapper.selectById(uid);
        if (myUser.getStatus() != 5) {
            response.sendError(401, "no Permission");
            return false;
        }
        else {
            request.setAttribute("uid", uid);
            return true;
        }
    }
}
