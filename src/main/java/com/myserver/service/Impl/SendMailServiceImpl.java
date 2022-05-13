package com.myserver.service.Impl;

import com.myserver.Pojo.EmailCode;
import com.myserver.utils.CodeUtils;
import com.myserver.service.SendMailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 类型：Service
 * 作用：发送邮件服务层
 *
 * @author 张天奕
 * @see CodeUtils
 */
@Service
@Data
public class SendMailServiceImpl implements SendMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private CodeUtils codeUtils;
    //发送人
    private String fromWho = "youdoufuz@qq.com";
    //接收人
    private String toWho = "1161085395@qq.com";
    //标题
    private String subject = "油豆腐树洞";
    //正文

    /**
     * 发送邮件核心业务
     *
     * @param email 发送目标邮箱地址
     * @param method 是注册还是找回
     * @return 成功就缓存验证码
     */
    @Override
    //必须得用异步，要不然得等半天
    //确实还得同步，反正前端不得等发送完邮件再告诉用户发送成功
    //以key值为email存code值 不为空的时候才存
    @CachePut(value = "emailCode", key = "#email", unless = "#result==\"failed\"")
    public String sendCodeToEmail(String email, String method) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        System.out.println(ops.get("emailCode::" + email));
        if (ops.get("emailCode::" + email) != null) {
            return "failed";
        }
        //生成验证码
        String code = codeUtils.MD5(email + new Date());
        code = code.substring(code.length() - 5);
        //发送验证码
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromWho + "(油豆腐Official)");
        message.setTo(email);
        if (method.equals("register")) {
            message.setSubject(subject + "注册验证码");
            message.setText(subject + "注册验证码: " + code + " 五分钟内有效哦");
        }
        else if (method.equals("changePassword")) {
            message.setSubject(subject + "验证码");
            message.setText(subject + "验证码: " + code + " 五分钟内有效哦");
        }
        javaMailSender.send(message);
        return code;
    }

    /**
     * 校验验证码
     *
     * @param emailCode 该邮箱的验证码 {@link EmailCode}
     * @return 输入的验证码和缓存中是否相等
     */
    @Override
    public Boolean validateCode(EmailCode emailCode) {
        String code = emailCode.getCode();
        String cacheCode = codeUtils.getCode(emailCode.getEmail());
        return code.equals(cacheCode);
    }

}
