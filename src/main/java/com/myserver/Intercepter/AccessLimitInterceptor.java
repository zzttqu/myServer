package com.myserver.Intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myserver.config.myannotation.AccessLimit;
import com.myserver.utils.IpGetter;
import com.myserver.utils.R;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("拦截了次数");
        //只有被注释了的controller才会按注释被拦截
        //没有注释的就按默认拦截
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            Duration seconds;
            int maxCount;
            String ip = IpGetter.getRemoteHost(request);
            String key = ip + "::" + request.getServletPath();
            String rawCount = stringRedisTemplate.opsForValue().get(key);
            if (accessLimit == null) {
                //如果没有注释就给一个默认的拦截次数
                seconds = Duration.ofSeconds(60);
                maxCount = 10;
            }
            else {
                seconds = Duration.ofSeconds(accessLimit.seconds());
                maxCount = accessLimit.maxCount();
            }
//            boolean needLogin = accessLimit.needLogin();
//            if (needLogin) {
//                request.getSession().getAttribute("uid");
//                //判断是否登录
//            }
            //首次进入
            if (rawCount == null || "-1".equals(rawCount)) {
                stringRedisTemplate.opsForValue().set(key, "1", seconds);
                return true;
            }
            int count = Integer.parseInt(rawCount);
            //访问次数小于最大次数
            if (count < maxCount) {
                stringRedisTemplate.opsForValue().increment(key, 1);
                return true;
            }
            if (count > 30) {
                log.info("ip " + ip + " 请求次数过高，需要注意");
            }
            stringRedisTemplate.opsForValue().increment(key, 1);
            R r = new R(403, "请求过于频繁");
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(r);
            //大于等于最大次数
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(s);
            return false;
        }
        return true;
    }
}