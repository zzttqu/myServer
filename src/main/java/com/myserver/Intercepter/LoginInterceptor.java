package com.myserver.Intercepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username= (String) request.getSession().getAttribute("username");
        Integer uid= (Integer) request.getSession().getAttribute("uid");
        if (username==null){
            response.sendError(401,"no Permission");
            return false;
        }
        else {
            request.setAttribute("uid",uid);
            return true;
        }

    }
}
